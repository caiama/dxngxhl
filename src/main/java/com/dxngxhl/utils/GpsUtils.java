public class GpsUtils {

    private static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
    // ��
    private static double pi = 3.1415926535897932384626;
    // ������
    private static double a = 6378245.0;
    // ����
    private static double ee = 0.00669342162296594323;

    public static boolean out_of_china(double lon, double lat) {
        if(lon < 72.004 || lon > 137.8347) {
            return true;
        } else if(lat < 0.8293 || lat > 55.8271) {
            return true;
        }
        return false;
    }

    public static double transformlat(double lon, double lat) {
        double ret = -100.0 + 2.0 * lon + 3.0 * lat + 0.2 * lat * lat + 0.1 * lon * lat + 0.2 * Math.sqrt(Math.abs(lon));
        ret += (20.0 * Math.sin(6.0 * lon * pi) + 20.0 * Math.sin(2.0 * lon * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lat * pi) + 40.0 * Math.sin(lat / 3.0 * pi)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(lat / 12.0 * pi) + 320 * Math.sin(lat * pi / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    public static double transformlng(double lon, double lat) {
        double ret = 300.0 + lon + 2.0 * lat + 0.1 * lon * lon + 0.1 * lon * lat + 0.1 * Math.sqrt(Math.abs(lon));
        ret += (20.0 * Math.sin(6.0 * lon * pi) + 20.0 * Math.sin(2.0 * lon * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lon * pi) + 40.0 * Math.sin(lon / 3.0 * pi)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(lon / 12.0 * pi) + 300.0 * Math.sin(lon / 30.0 * pi)) * 2.0 / 3.0;
        return ret;
    }

    /**
     * WGS84תGCJ02(��������ϵ)
     *
     * @param wgs_lon WGS84����ϵ�ľ���
     * @param wgs_lat WGS84����ϵ��γ��
     * @return ������������
     */
    public static double[] wgs84togcj02(double wgs_lon, double wgs_lat) {
        if (out_of_china(wgs_lon, wgs_lat)) {
            return new double[] { wgs_lon, wgs_lat };
        }
        double dlat = transformlat(wgs_lon - 105.0, wgs_lat - 35.0);
        double dlng = transformlng(wgs_lon - 105.0, wgs_lat - 35.0);
        double radlat = wgs_lat / 180.0 * pi;
        double magic = Math.sin(radlat);
        magic = 1 - ee * magic * magic;
        double sqrtmagic = Math.sqrt(magic);
        dlat = (dlat * 180.0) / ((a * (1 - ee)) / (magic * sqrtmagic) * pi);
        dlng = (dlng * 180.0) / (a / sqrtmagic * Math.cos(radlat) * pi);
        double mglat = wgs_lat + dlat;
        double mglng = wgs_lon + dlng;
        return new double[] { mglng, mglat };
    }

    /**
     * GCJ02(��������ϵ)תGPS84
     *
     * @param gcj_lon ��������ϵ�ľ���
     * @param gcj_lat ��������ϵγ��
     * @return WGS84��������
     */
    public static double[] gcj02towgs84(double gcj_lon, double gcj_lat) {
        if (out_of_china(gcj_lon, gcj_lat)) {
            return new double[] { gcj_lon, gcj_lat };
        }
        double dlat = transformlat(gcj_lon - 105.0, gcj_lat - 35.0);
        double dlng = transformlng(gcj_lon - 105.0, gcj_lat - 35.0);
        double radlat = gcj_lat / 180.0 * pi;
        double magic = Math.sin(radlat);
        magic = 1 - ee * magic * magic;
        double sqrtmagic = Math.sqrt(magic);
        dlat = (dlat * 180.0) / ((a * (1 - ee)) / (magic * sqrtmagic) * pi);
        dlng = (dlng * 180.0) / (a / sqrtmagic * Math.cos(radlat) * pi);
        double mglat = gcj_lat + dlat;
        double mglng = gcj_lon + dlng;
        return new double[] { gcj_lon * 2 - mglng, gcj_lat * 2 - mglat };
    }


    /**
     * ��������ϵ(GCJ-02)ת�ٶ�����ϵ(BD-09)
     *
     * �ȸ衢�ߵ¡���>�ٶ�
     * @param gcj_lon �������꾭��
     * @param gcj_lat ��������γ��
     * @return �ٶ���������
     */
    public static double[] gcj02tobd09(double gcj_lon, double gcj_lat) {
        double z = Math.sqrt(gcj_lon * gcj_lon + gcj_lat * gcj_lat) + 0.00002 * Math.sin(gcj_lat * x_pi);
        double theta = Math.atan2(gcj_lat, gcj_lon) + 0.000003 * Math.cos(gcj_lon * x_pi);
        double bd_lng = z * Math.cos(theta) + 0.0065;
        double bd_lat = z * Math.sin(theta) + 0.006;
        return new double[] { bd_lng, bd_lat };
    }

    /**
     * �ٶ�����ϵ(BD-09)ת��������ϵ(GCJ-02)
     *
     * �ٶȡ���>�ȸ衢�ߵ�
     * @param bd_lon �ٶ�����γ��
     * @param bd_lat �ٶ����꾭��
     * @return ������������
     */
    public static double[] bd09togcj02(double bd_lon, double bd_lat) {
        double x = bd_lon - 0.0065;
        double y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        double gg_lng = z * Math.cos(theta);
        double gg_lat = z * Math.sin(theta);
        return new double[] { gg_lng, gg_lat };
    }

    /**
     * WGS����ת�ٶ�����ϵ(BD-09)
     *
     * @param wgs_lng WGS84����ϵ�ľ���
     * @param wgs_lat WGS84����ϵ��γ��
     * @return �ٶ���������
     */
    public static double[] wgs84tobd09(double wgs_lng, double wgs_lat) {
        double[] gcj = wgs84togcj02(wgs_lng, wgs_lat);
        double[] bd09 = gcj02tobd09(gcj[0], gcj[1]);
        return bd09;
    }

    /**
     * �ٶ�����ϵ(BD-09)תWGS����
     *
     * @param bd_lng �ٶ�����γ��
     * @param bd_lat �ٶ����꾭��
     * @return WGS84��������
     */
    public static double[] bd09towgs84(double bd_lng, double bd_lat) {
        double[] gcj = bd09togcj02(bd_lng, bd_lat);
        double[] wgs84 = gcj02towgs84(gcj[0], gcj[1]);
        return wgs84;
    }
}