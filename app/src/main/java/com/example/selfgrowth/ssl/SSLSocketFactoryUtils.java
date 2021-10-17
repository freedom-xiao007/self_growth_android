package com.example.selfgrowth.ssl;

import android.content.Context;
import android.content.res.AssetManager;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * @Author:AbnerMing
 * @Description:
 * @Date:2019/11/7 8:53
 */
public class SSLSocketFactoryUtils extends AppCompatActivity {
    private static final String serverCrt = "-----BEGIN CERTIFICATE-----\n" +
            "MIIDjTCCAnUCFF25cs9P5LyAbjDjDqveF9q/OOsHMA0GCSqGSIb3DQEBCwUAMIGC\n" +
            "MQswCQYDVQQGEwJDTjEQMA4GA1UECAwHQmVpamluZzEQMA4GA1UEBwwHQmVpamlu\n" +
            "ZzEMMAoGA1UECgwDT25lMQswCQYDVQQLDAJJVDESMBAGA1UEAwwJbG9jYWxob3N0\n" +
            "MSAwHgYJKoZIhvcNAQkBFhFleGFtcGxlQGVtYWlsLmNvbTAeFw0yMTEwMTYxMzI4\n" +
            "MTBaFw0yMjEwMTYxMzI4MTBaMIGCMQswCQYDVQQGEwJDTjEQMA4GA1UECAwHQmVp\n" +
            "amluZzEQMA4GA1UEBwwHQmVpamluZzEMMAoGA1UECgwDT25lMQswCQYDVQQLDAJJ\n" +
            "VDESMBAGA1UEAwwJbG9jYWxob3N0MSAwHgYJKoZIhvcNAQkBFhFleGFtcGxlQGVt\n" +
            "YWlsLmNvbTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAK0tLJilHsvp\n" +
            "1RwuRQip26hcCQesclgbIXOrUraEyRvIYV+0bdkatv6qv8cHv300l41xsR03qj1H\n" +
            "wHep0z81t4QUuSBZ3I/I9JyY7XbCwtwtV2WevhQPTpH33TpYB6d2CmYdy6bxTCXv\n" +
            "hPUQLa3qUTBpk7cM2p5FxUuTuFo4w1C8CELP9ZxAfrFewC+DK9DmWhDOzrrqcw68\n" +
            "1kk1MdmY/CrlEsbb9y1coAMx9ghuCE9h02gN74TpYtT7fCteSsL7vwqF3vaALzGa\n" +
            "H+iYaw39nUWwFrmcscNev36IjZk/GeYSwq4y27BgcdGAD5KNDtGddLhYmcW4AmFH\n" +
            "8vj8v1Zi2ScCAwEAATANBgkqhkiG9w0BAQsFAAOCAQEAFAHAG9aDrGDDw9bBx7Vf\n" +
            "xow46ngCtCIv0D3x+ukbbJ2EJ3pPjku8w9Yvl4LCNn/8bKqqqTXYPyZmaKckrA8Q\n" +
            "P1z3EJGmI08eBdferYtP4K3JF73nHBxCpD0UKMmOz/kJI1YONJIg104cKz8UIEVG\n" +
            "wxCxsR4lq6p/6JxVMCj1I1t+g/KqTB/SRiWUNeqmIyTMR+muBQpoNY7YMJzmvG5e\n" +
            "CFFrmS1OMl6ZsPdsb2WK1st2pcwzTz34Wa0vD93bynXoMA7is7gsNqqzPcNYYCxf\n" +
            "VRevd0ZjymRMf/vAw47h8GA4hHYMw6E6KmiCJGkqZyMrIo0zEsZ8eLhElML34niQ\n" +
            "qA==\n" +
            "-----END CERTIFICATE-----";

    private static final String HOST_NAME = "localhost";//请求服务器的主机名称
    private static String CERTIFICATE_NAME = "server.crt";//下载的证书的文件名称
    //证书
    private X509Certificate x509Certificate;
    //需要配置给ok的SSLSocketFactory
    private SSLSocketFactory mSslSocketFactory;
    private SSLContext sslContext;

    //证书管理者
    private MyTrustManager mTrustManager;

    private static  SSLSocketFactoryUtils instance;

    private SSLSocketFactoryUtils() {
        initSslSocketFactory();
    }

    private  void initSslSocketFactory(){
        try {
            sslContext = SSLContext.getInstance("TLS");
            //从assets文件夹下根据证书名字读取证书,变成一个可用的证书对象
            x509Certificate = readCert(null, CERTIFICATE_NAME);
            //校验服务端和本地证书是否一致
            mTrustManager = new MyTrustManager(x509Certificate);
            //初始化必要的对象,固定格式直接使用即可
            sslContext.init(null, new TrustManager[]{
                    mTrustManager
            }, new java.security.SecureRandom());
            mSslSocketFactory = sslContext.getSocketFactory();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static SSLSocketFactoryUtils getInstance() {
        if (instance == null) {
            instance=new SSLSocketFactoryUtils();
        }
        return instance;
    }

    /**
     * 根据asset下证书的名字取出证书,然后变成流,在变成证书对象
     */
    private static X509Certificate readCert(Context context, String assetName) {
        InputStream inputStream = null;
        //            inputStream = context.getAssets().open(assetName);
//            inputStream = assetManager.open("server.crt");
        inputStream = new ByteArrayInputStream(serverCrt.getBytes(StandardCharsets.UTF_8));
        X509Certificate cert = null;
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            cert = (X509Certificate) cf.generateCertificate(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Throwable ex) {
            }
        }
        return cert;
    }

    public HostnameVerifier getHostnameVerifier() {
        return hostnameVerifier;
    }

    public SSLSocketFactory getmSslSocketFactory() {
        return mSslSocketFactory;
    }

    public MyTrustManager getmTrustManager() {
        return mTrustManager;
    }

    /**
     * 实现了 X509TrustManager
     * 通过此类中的 checkServerTrusted 方法来确认服务器证书是否正确
     */
    private static final class MyTrustManager implements X509TrustManager {
        X509Certificate cert;

        MyTrustManager(X509Certificate cert) {
            this.cert = cert;
        }

        @Override// 我们在客户端只做服务器端证书校验。
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        /**
         * @param chain 服务端返回的证书数组,因为服务器可能有多个https证书,我们在这里的
         *              逻辑就是拿到第一个证书,然后和本地证书判断,如果不一致,异常!!!
         */
        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//             确认服务器端证书和代码中 hard code 的 CRT 证书相同。
//            这里因为我们服务器只有一个证书,没有遍历,如果有多个,这里是for循环取出挨个判断
            if (chain[0].equals(this.cert)) {
                return;
            }
            throw new CertificateException("checkServerTrusted No trusted server cert found!");
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[]{};
        }
    }

    /**
     * 服务器域名验证,拿到请求接口的域名和本地配的域名进行比较,如果一样返回True
     */
    private  final HostnameVerifier hostnameVerifier = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return hostname.equals(HOST_NAME);
            //return true;
        }
    };
}
