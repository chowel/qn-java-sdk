package me.lj.qiniu.dora;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.processing.OperationManager;
import com.qiniu.storage.Configuration;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.qiniu.util.UrlSafeBase64;
import me.lj.qiniu.config.Config;

public class PfopVframe {

    public static void main(String[] args) throws QiniuException {
        //设置账号的AK,SK
        String ACCESS_KEY = Config.ACCESS_KEY;
        String SECRET_KEY = Config.SECRET_KEY;
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        //新建一个OperationManager对象
        OperationManager operater = new OperationManager(auth, new Configuration(Zone.autoZone()));
        //设置要转码的空间和key，并且这个key在你空间中存在
        String bucket = "test-pub";
        //视频名
        String key = "11.mp4";
        //图片名
        String newkey = "png/vframe/11.png";
        String newkey1 = "png/vframe/22.png";
        //设置转码操作参数
        String fops = "vframe/jpg/offset/1/w/480/h/360/rotate/90";
        String fops1 = "vframe/jpg/offset/1/w/720/h/480/rotate/180";
        //设置转码的队列
        String pipeline = "12349";
        //可以对转码后的文件进行使用saveas参数自定义命名，当然也可以不指定文件会默认命名并保存在当前空间。
        String urlbase64 = UrlSafeBase64.encodeToString(bucket + ":" + newkey);
        String urlbase641 = UrlSafeBase64.encodeToString(bucket + ":" + newkey1);
        String pfops = fops + "|saveas/" + urlbase64;
        pfops = pfops + ";" +fops1 + "|saveas/" + urlbase641;
        //设置pipeline参数
        StringMap params = new StringMap().putWhen("force", 1, true).putNotEmpty("pipeline", pipeline);
        try {
            String persistid = operater.pfop(bucket, key, pfops, params);
            //打印返回的persistid
            System.out.println(persistid);
        } catch (QiniuException e) {
            //捕获异常信息
            Response r = e.response;
            // 请求失败时简单状态信息
            System.out.println(r.toString());
            try {
                // 响应的文本信息
                System.out.println(r.bodyString());
            } catch (QiniuException e1) {
                //ignore
            }
        }
    }
}