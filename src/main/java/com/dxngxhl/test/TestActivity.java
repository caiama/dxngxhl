package com.dxngxhl.test;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.dxngxhl.R;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Cool on 2017/7/11.
 *  RxJava常用操作符
 * https://mp.weixin.qq.com/s?__biz=MzIwMzYwMTk1NA==&mid=2247484203&idx=1&sn=6c98747e0c2d3bd4a7001d2db45df6d8&chksm=96cda266a1ba2b7086f38adb26135009a59dd42c58be4f9c60ed0e56b37952b2d5919173d783&scene=21#wechat_redirect
 */

public class TestActivity extends Activity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //rxjava2
//        demo1();
//        demo2();
//        demo3();
//        demo4();
//        demo5();
//        demo6();
        demo7();
    }
    /**
     *  响应式拉取
     *  request
     *  异步线程依旧有内存问题
     *  BackpressureStrategy.ERROR:最大128个
     *  BackpressureStrategy.BUFFER：没有大小限制
     *  BackpressureStrategy.DROP：把存不下的事件丢弃
     *  BackpressureStrategy.LATEST：只保留最新的事件
     *
     *  FlowableEmitter
     * */
    public void demo7(){
        Flowable<Integer> flowable = Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                Log.i("","===subscribe1");
                e.onNext(2);
                Log.i("","===subscribe2");
                e.onNext(3);
                Log.i("","===subscribe3");
                //下游Subscription.request()的结果
                e.requested();
            }
        }, BackpressureStrategy.ERROR);
        Subscriber<Integer> subsceible = new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                Log.i("","===onSubscribe");
                //一次请求多少事件
                s.request(2);
            }

            @Override
            public void onNext(Integer integer) {
                Log.i("","===onNext:"+integer);
            }

            @Override
            public void onError(Throwable t) {
                Log.i("","===onError");
            }

            @Override
            public void onComplete() {
                Log.i("","===onComplete");
            }
        };
        flowable.subscribe(subsceible);
    }
    /**
     *  Backpressure
     *  水缸--异步
     *  3种解决方法
     * */
    public void demo6(){
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                for (int i = 0;  ; i++) {
                    e.onNext(i);
                    Thread.sleep(5);    //上游每次发送完事件后都延时了5秒,
                }
            }
        }).subscribeOn(Schedulers.io())
//                .filter(new Predicate<Integer>() {
//                    @Override
//                    public boolean test(@NonNull Integer integer) throws Exception {
//                        //filter, 只允许能被100整除的事件通过
//                        return integer%100==0;
//                    }
//                })
//                .sample(5,TimeUnit.SECONDS)         //每隔5秒取一个事件给下游
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        Log.i("","===accept"+integer);
                    }
                });
    }

    /**
     *  Zip操作符---按顺序进行组合---
     *  zip发送的事件数量和数量最少的Observable有关
     *  场景--需要两个条件才能下一步操作
     * */
    public void demo5(){
        Observable<Integer> ob1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                Log.i("","===1-onNext-1");
                Thread.sleep(100);
                e.onNext(2);
                Log.i("","===1-onNext-2");
                Thread.sleep(100);
                e.onNext(3);
                Log.i("","===1-onNext-3");
                Thread.sleep(100);
                e.onComplete();
                Log.i("","===oncomplete1");
                Thread.sleep(100);
            }
        }).subscribeOn(Schedulers.io());
        Observable<String> ob2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                e.onNext("A");
                Thread.sleep(100);
                Log.i("","===2-onNext-A");
                e.onNext("B");
                Thread.sleep(100);
                Log.i("","===2-onNext-B");
                e.onNext("C");
                Thread.sleep(100);
                Log.i("","===2-onNext-C");
                e.onNext("D");
                Thread.sleep(100);
                Log.i("","===2-onNext-D");
                e.onComplete();
                Thread.sleep(100);
                Log.i("","===oncomplete2");
            }
        }).subscribeOn(Schedulers.io());
        Observable.zip(ob1, ob2, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(@NonNull Integer integer, @NonNull String s) throws Exception {
                return integer+s;
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull String s) {
                Log.i("","===3-onnext:"+s);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {
                Log.i("","===oncomplete3");
            }
        });
    }

    //flatMap操作符--结果无序，concatMap操作符结果有序
    //filter:过滤
    public void demo4(){
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
            }
        }).flatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(@NonNull Integer integer) throws Exception {
                List<String> list = new ArrayList<String>();
                for (int i = 0; i < 3; i++) {
                    list.add("这是flatMap - - - :"+integer);
                }
                return Observable.fromIterable(list).delay(10, TimeUnit.MILLISECONDS);
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                Log.i("","===Flatmap---accept"+s);
            }
        });
    }

    //map操作符
    public void demo3(){
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
            }
        }).map(new Function<Integer,String>(){

            @Override
            public String apply(@NonNull Integer integer) throws Exception {
                return "This is "+integer;
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                Log.i("","===accept"+s);
            }
        });
    }

    //线程控制
    public void demo2(){
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                e.onNext(2);
                Log.i("","===subscribe-thred:"+Thread.currentThread().getName());
            }
        });
        Consumer<Integer> consumer = new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                Log.i("","===accept"+integer);
                Log.i("","===accept-thred:"+Thread.currentThread().getName());
            }
        };
//        observable.subscribe(consumer);   //同一个线程-main
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer);
    }

    //普通
    public void demo1(){
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                //
                e.onNext(2);
                e.onNext(6);
//                e.onError(new NullPointerException());
                e.onNext(1);
                e.onComplete();
            }
        });
        Observer<Integer> observer = new Observer<Integer>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.i("","===onSubscribe");
            }

            @Override
            public void onNext(@NonNull Integer integer) {
                Log.i("","===onNext"+integer);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.i("","===onError");
            }

            @Override
            public void onComplete() {
                Log.i("","===onComplete");
            }
        };
        observable.subscribe(observer);
    }
}
