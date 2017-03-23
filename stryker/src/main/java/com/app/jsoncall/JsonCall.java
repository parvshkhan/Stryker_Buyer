package com.app.jsoncall;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonCall {
    String res, reslatlon;
    FileInputStream in = null;

    public String verifyCode(String sellerId, String verificationCode, String url) {

        Log.e("seller_id", sellerId);
        Log.e("verification_code", verificationCode);
        Log.e("url", url);

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), 10000); // Timeout
            HttpPost httppost = new HttpPost(url);
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("seller_id", sellerId));
            nameValuePairs.add(new BasicNameValuePair("verification_code", verificationCode));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                res = EntityUtils.toString(resEntity);
                // .....Read the response
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;
    }

    public String resendCode(String sellerId, String url) {

        Log.e("seller_id", sellerId);
        Log.e("url", url);

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), 10000); // Timeout
            HttpPost httppost = new HttpPost(url);
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("seller_id", sellerId));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                res = EntityUtils.toString(resEntity);
                // .....Read the response
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;

    }

    public String showNotifications(String sellerId, String storeId, String url) {

        Log.e("seller_id", sellerId);
        Log.e("store_id", storeId);
        Log.e("url", url);

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), 10000); // Timeout
            HttpPost httppost = new HttpPost(url);
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

            nameValuePairs.add(new BasicNameValuePair("seller_id", sellerId));
            nameValuePairs.add(new BasicNameValuePair("store_id", storeId));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                res = EntityUtils.toString(resEntity);
                // .....Read the response
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;
    }


    public String getDashboard(String url) {
        Log.e("url***", "***" + url);
        String response = "";
        DefaultHttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); // Timeout
        // HttpResponse response;
        JSONObject json = new JSONObject();
        try {
            HttpGet get = new HttpGet(url);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            response = client.execute(get, responseHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

	/*public String getLogin(String logintype, String email, String password,
            String fullname, String age, String gender,
			String phone, String socialid, String latitude,
			String longitude, String deviceToken, String url) {*/


    public String getLogin(String loginId, String password, String latitude,
                           String longitude, String device_id, String device_type, String url) {


        // http: //
        // services.appliconsultants.com/stryker/web/service/login?
        //user_role=1&login_type=1&email=seller01@gmail.com&password=admin&
        //full_name=Amit%20Sinha&age=32&gender=M&phone=9555436989&
        //socialid=1&latitude=21.12345&longitude=77.12345&
        //device_id=did1&device_type=android

        //User Role can be 1->Admin, 2->Seller, 3->Buyer
        //Login Type can be 1->Normal, 2->Facebook, 3->Google

        Log.e("loginId", loginId);
        Log.e("password", password);
        Log.e("latitude", latitude);
        Log.e("longitude", longitude);
        Log.e("device_id", device_id);
        Log.e("device_type", device_type);
        Log.e("url", url);

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), 10000); // Timeout
            HttpPost httppost = new HttpPost(url);
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(13);
            nameValuePairs.add(new BasicNameValuePair("loginId", loginId));
            nameValuePairs.add(new BasicNameValuePair("password", password));
            nameValuePairs.add(new BasicNameValuePair("latitude", latitude));
            nameValuePairs.add(new BasicNameValuePair("password", password));
            nameValuePairs.add(new BasicNameValuePair("longitude", longitude));
            nameValuePairs.add(new BasicNameValuePair("device_id", device_id));
            nameValuePairs.add(new BasicNameValuePair("device_type", "android"));

			/*nameValuePairs.add(new BasicNameValuePair("phone", phone));
			nameValuePairs.add(new BasicNameValuePair("socialid", socialid));
			nameValuePairs.add(new BasicNameValuePair("latitude", latitude));
			nameValuePairs.add(new BasicNameValuePair("longitude", longitude));
			nameValuePairs.add(new BasicNameValuePair("device_id",
					deviceToken));
			nameValuePairs
					.add(new BasicNameValuePair("device_type", "android"));
*/
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                res = EntityUtils.toString(resEntity);
                // .....Read the response
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;

    }


    public String getRegister(String loginId, String password,
                              String full_name, String latitude, String longitude,
                              String device_id, String device_type, String user_role, String url) {

        // http: //
        // services.appliconsultants.com/stryker/web/service/login?
        //user_role=1&login_type=1&email=seller01@gmail.com&password=admin&
        //full_name=Amit%20Sinha&age=32&gender=M&phone=9555436989&
        //socialid=1&latitude=21.12345&longitude=77.12345&
        //device_id=did1&device_type=android

        //User Role can be 1->Admin, 2->Seller, 3->Buyer
        //Login Type can be 1->Normal, 2->Facebook, 3->Google

        Log.e("loginId", loginId);
        Log.e("password", password);
        Log.e("full_name", full_name);
        Log.e("latitude", latitude);
        Log.e("longitude", longitude);
        Log.e("device_id", device_id);
        Log.e("device_type", device_type);
        Log.e("user_role", "");
        Log.e("url", url);

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), 10000); // Timeout
            HttpPost httppost = new HttpPost(url);
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(13);
            nameValuePairs.add(new BasicNameValuePair("loginId", loginId));
            nameValuePairs.add(new BasicNameValuePair("password", password));
            nameValuePairs.add(new BasicNameValuePair("full_name", full_name));
            nameValuePairs.add(new BasicNameValuePair("latitude", latitude));
            nameValuePairs.add(new BasicNameValuePair("longitude", longitude));
            nameValuePairs.add(new BasicNameValuePair("device_id", device_id));
            nameValuePairs.add(new BasicNameValuePair("device_type", device_type));
            nameValuePairs.add(new BasicNameValuePair("user_role", user_role));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                res = EntityUtils.toString(resEntity);
                // .....Read the response
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;

    }


    public String homeData(String user_id, String device_id, String url) {

        Log.e("device_id", device_id);
        Log.e("user_id", user_id);

        Log.e("url", url);
        //device_id: "did1",
        //user_id: "13"
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), 10000); // Timeout
            HttpPost httppost = new HttpPost(url);
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("device_id", device_id));
            nameValuePairs.add(new BasicNameValuePair("user_id", user_id));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                res = EntityUtils.toString(resEntity);
                // .....Read the response
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;

    }

    public String removeStore(String user_id, String store_id, String url) {

        Log.e("store_id", store_id);
        Log.e("user_id", user_id);

        Log.e("url", url);
        //store_id: "did1",
        //user_id: "13"
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), 10000); // Timeout
            HttpPost httppost = new HttpPost(url);
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
            nameValuePairs.add(new BasicNameValuePair("user_id", user_id));
            nameValuePairs.add(new BasicNameValuePair("store_id", store_id));
            nameValuePairs.add(new BasicNameValuePair("user_type", "2"));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                res = EntityUtils.toString(resEntity);
                // .....Read the response
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;

    }

    public String updatechat(String user_id, String chat_id, String chat_login_id, String url) {

        Log.e("chat_id", chat_id);
        Log.e("chatLoginID", chat_login_id);
        Log.e("user_id", user_id);

        Log.e("url", url);
        //store_id: "did1",
        //user_id: "13"
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), 10000); // Timeout
            HttpPost httppost = new HttpPost(url);
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
            nameValuePairs.add(new BasicNameValuePair("user_id", user_id));
            nameValuePairs.add(new BasicNameValuePair("chat_id", chat_id));
            nameValuePairs.add(new BasicNameValuePair("chatLoginID", chat_login_id));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                res = EntityUtils.toString(resEntity);
                // .....Read the response
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;

    }

    public String cancelOrder(String order_id, String status_id, String user_id, String device_id, String url) {

        Log.e("order_id", order_id);
        Log.e("status_id", status_id);
        Log.e("user_id", user_id);
        Log.e("device_id", device_id);

        Log.e("url", url);
        //store_id: "did1",
        //user_id: "13"
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), 10000); // Timeout
            HttpPost httppost = new HttpPost(url);
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
            nameValuePairs.add(new BasicNameValuePair("order_id", order_id));
            nameValuePairs.add(new BasicNameValuePair("status_id", status_id));
            nameValuePairs.add(new BasicNameValuePair("user_id", user_id));
            nameValuePairs.add(new BasicNameValuePair("device_id", device_id));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                res = EntityUtils.toString(resEntity);
                // .....Read the response
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;

    }

    public String checkOut(String orderJson, String addressJson, String productJson, String url) {

        Log.e("order_json", orderJson);
        Log.e("address_json", addressJson);
        Log.e("product_json", productJson);

        Log.e("url", url);
        //device_id: "did1",
        //user_id: "13"
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), 10000); // Timeout
            HttpPost httppost = new HttpPost(url);
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            nameValuePairs.add(new BasicNameValuePair("order_json", orderJson));
            nameValuePairs.add(new BasicNameValuePair("address_json", addressJson));
            nameValuePairs.add(new BasicNameValuePair("product_json", productJson));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                res = EntityUtils.toString(resEntity);
                // .....Read the response
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;

    }

    public String deleteAddress(String user_id, String address_id, String url) {

        Log.e("user_id", user_id);
        Log.e("address_id", address_id);
        Log.e("url", url);

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), 10000); // Timeout
            HttpPost httppost = new HttpPost(url);
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            nameValuePairs.add(new BasicNameValuePair("user_id", user_id));
            nameValuePairs.add(new BasicNameValuePair("address_id", address_id));


            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                res = EntityUtils.toString(resEntity);
                // .....Read the response
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;

    }

    public String ResetMobilePassword(String MobileNo, String Password, String device_id, String url) {

        Log.e("loginId", MobileNo);

        Log.e("url", url);
        // http://services.appliconsultants.com/inffo/web/service/forgetpassword?email=aksinha1982@gmail.com
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), 10000); // Timeout
            HttpPost httppost = new HttpPost(url);
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("loginId", MobileNo));
            nameValuePairs.add(new BasicNameValuePair("password", Password));
            nameValuePairs.add(new BasicNameValuePair("cpassword", device_id));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                res = EntityUtils.toString(resEntity);
                // .....Read the response
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;

    }

    public String forgotPassword(String loginId, String url) {

        Log.e("loginId", loginId);

        Log.e("url", url);
        // http://services.appliconsultants.com/inffo/web/service/forgetpassword?email=aksinha1982@gmail.com
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), 10000); // Timeout
            HttpPost httppost = new HttpPost(url);
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("loginId", loginId));
            nameValuePairs.add(new BasicNameValuePair("roletype", "3"));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                res = EntityUtils.toString(resEntity);
                // .....Read the response
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;

    }

    public String countryList(String url) {

        Log.e("url", url);
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), 10000); // Timeout
            HttpPost httppost = new HttpPost(url);
            // Add your data

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                res = EntityUtils.toString(resEntity);
                // .....Read the response
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;

    }

    public String getProductList(String store_id, String url) {

        Log.e("store_id", store_id);

        Log.e("url", url);
        // http://services.appliconsultants.com/inffo/web/service/forgetpassword?email=aksinha1982@gmail.com
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), 10000); // Timeout
            HttpPost httppost = new HttpPost(url);
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("store_id", store_id));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                res = EntityUtils.toString(resEntity);
                // .....Read the response
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;

    }

    public String addAddress(String user_id, String address_json, String url) {

        //user_id and address_json
        Log.e("user_id", user_id);
        Log.e("address_json", address_json);
        Log.e("url", url);
        // http://services.appliconsultants.com/inffo/web/service/forgetpassword?email=aksinha1982@gmail.com
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), 10000); // Timeout
            HttpPost httppost = new HttpPost(url);
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            nameValuePairs.add(new BasicNameValuePair("user_id", user_id));
            nameValuePairs.add(new BasicNameValuePair("address_json", address_json));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                res = EntityUtils.toString(resEntity);
                // .....Read the response
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;

    }

    public String storeList(String url) {


        Log.e("url", url);
        // http://services.appliconsultants.com/inffo/web/service/forgetpassword?email=aksinha1982@gmail.com
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), 10000); // Timeout
            HttpPost httppost = new HttpPost(url);
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                res = EntityUtils.toString(resEntity);
                // .....Read the response
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;

    }

    public String addStore(String user_id, String store_id, String device_id, String url) {
        Log.e("url", url);
        //http://services.appliconsultants.com/stryker/web/service/addbuyerstore?buyer_id=&store_id=1&device_id=did3
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), 100000); // Timeout
            HttpPost httppost = new HttpPost(url);
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            nameValuePairs.add(new BasicNameValuePair("buyer_id", user_id));
            nameValuePairs.add(new BasicNameValuePair("store_id", store_id));
            nameValuePairs.add(new BasicNameValuePair("device_id", device_id));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                res = EntityUtils.toString(resEntity);
                // .....Read the response
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;

    }

    public String verifyContacts(String val,String url) {
        Log.e("url", url);
        //http://services.appliconsultants.com/stryker/web/service/addbuyerstore?buyer_id=&store_id=1&device_id=did3
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), 100000); // Timeout
            HttpPost httppost = new HttpPost(url);
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            nameValuePairs.add(new BasicNameValuePair("user_phone", val));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                res = EntityUtils.toString(resEntity);
                // .....Read the response
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;

    }


    public String getMasterData(String url) {
        Log.e("url", url);
        //http://services.appliconsultants.com/stryker/web/service/addbuyerstore?buyer_id=&store_id=1&device_id=did3
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), 10000); // Timeout
            HttpPost httppost = new HttpPost(url);
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                res = EntityUtils.toString(resEntity);
                // .....Read the response
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;

    }


    public String inviteStore(String user_id, String business_name, String phone_no, String city, String url) {
        Log.e("url", url);
        //http://services.appliconsultants.com/stryker/web/service/addbuyerstore?buyer_id=&store_id=1&device_id=did3
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), 10000); // Timeout
            HttpPost httppost = new HttpPost(url);
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            nameValuePairs.add(new BasicNameValuePair("user_id", user_id));
            nameValuePairs.add(new BasicNameValuePair("business_name", business_name));
            nameValuePairs.add(new BasicNameValuePair("phone_no", phone_no));
            nameValuePairs.add(new BasicNameValuePair("city", city));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                res = EntityUtils.toString(resEntity);
                // .....Read the response
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;

    }

    public String favInstituteList(String userId, String url) {
        Log.e("loggedin_user_id", userId);
        Log.e("url", url);
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), 10000); // Timeout
            HttpPost httppost = new HttpPost(url);
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("loggedin_user_id",
                    userId));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                res = EntityUtils.toString(resEntity);
                // .....Read the response
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;

    }

    public String searchInstitutes(String userId, String region_id,
                                   String distance, String latitude, String longitude,
                                   String page_num, String sort_by, String url) {

        Log.e("loggedin_user_id", userId);
        Log.e("region_id", region_id);
        Log.e("distance", distance);
        Log.e("latitude", latitude);
        Log.e("longitude", longitude);
        Log.e("page_num", page_num);
        Log.e("sort_by", sort_by);
        Log.e("url", url);

        Log.e("url", url);
        // loggedin_user_id=&region_id=&distance=&latitude=&longitude=&page_num=&sort_by=
        // if not login sort_by = 2 else 1. distance = 200
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), 10000); // Timeout
            HttpPost httppost = new HttpPost(url);
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
            nameValuePairs.add(new BasicNameValuePair("loggedin_user_id",
                    userId));
            nameValuePairs.add(new BasicNameValuePair("region_id", region_id));
            nameValuePairs.add(new BasicNameValuePair("distance", distance));
            nameValuePairs.add(new BasicNameValuePair("latitude", latitude));
            nameValuePairs.add(new BasicNameValuePair("longitude", longitude));
            nameValuePairs.add(new BasicNameValuePair("page_num", page_num));
            nameValuePairs.add(new BasicNameValuePair("sort_by", sort_by));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                res = EntityUtils.toString(resEntity);
                // .....Read the response
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;

    }

    public String instituteDetail(String institute_id, String latitude,
                                  String longitude, String loggedin_user_id, String url) {

        Log.e("institute_id", institute_id);
        Log.e("latitude", latitude);
        Log.e("longitude", longitude);
        Log.e("loggedin_user_id", loggedin_user_id);
        Log.e("url", url);

        Log.e("url", url);
        // institute_id=1&latitude=44.312603&longitude=23.812012&loggedin_user_id
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), 10000); // Timeout
            HttpPost httppost = new HttpPost(url);
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
            nameValuePairs.add(new BasicNameValuePair("institute_id",
                    institute_id));
            nameValuePairs.add(new BasicNameValuePair("latitude", latitude));
            nameValuePairs.add(new BasicNameValuePair("longitude", longitude));
            nameValuePairs.add(new BasicNameValuePair("loggedin_user_id",
                    loggedin_user_id));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                res = EntityUtils.toString(resEntity);
                // .....Read the response
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;

    }

    public String likeInstitute(String userId, String institute_id,
                                String to_do, String url) {

        Log.e("loggedin_user_id", userId);
        Log.e("institute_id", institute_id);
        Log.e("to_do", to_do);
        Log.e("url", url);

        Log.e("url", url);
        // loggedin_user_id=2&institute_id=1&to_do=1
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), 10000); // Timeout
            HttpPost httppost = new HttpPost(url);
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            nameValuePairs.add(new BasicNameValuePair("loggedin_user_id",
                    userId));
            nameValuePairs.add(new BasicNameValuePair("institute_id",
                    institute_id));
            nameValuePairs.add(new BasicNameValuePair("to_do", to_do));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                res = EntityUtils.toString(resEntity);
                // .....Read the response
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;

    }

    public String instituteViceLikes(String institute_id, String url) {
        Log.e("institute_id", institute_id);
        Log.e("url", url);
        Log.e("url", url);
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), 10000); // Timeout
            HttpPost httppost = new HttpPost(url);
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("institute_id",
                    institute_id));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                res = EntityUtils.toString(resEntity);
                // .....Read the response
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;

    }

    public String addDialog(String seller_id, String buyerId, String dialogId, String url) {

        Log.e("seller_id->", seller_id);
        Log.e("buyer_id", buyerId);
        Log.e("dialog_id", dialogId);
        Log.e("url", url);

        //seller_id: "1"

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), 10000); // Timeout
            HttpPost httppost = new HttpPost(url);
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);

            nameValuePairs.add(new BasicNameValuePair("seller_id", seller_id));
            nameValuePairs.add(new BasicNameValuePair("buyer_id", buyerId));
            nameValuePairs.add(new BasicNameValuePair("dialog_id", dialogId));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                res = EntityUtils.toString(resEntity);
                // .....Read the response
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;

    }

    public String getDialog(String seller_id, String buyerId, String url) {

        Log.e("seller_id->", seller_id);
        Log.e("buyer_id", buyerId);
        Log.e("url", url);
        //[11:33:35 PM] Deepak Sinha: seller_id
        //[11:33:39 PM] Deepak Sinha: buyer_id

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), 10000); // Timeout
            HttpPost httppost = new HttpPost(url);
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);

            nameValuePairs.add(new BasicNameValuePair("seller_id", seller_id));
            nameValuePairs.add(new BasicNameValuePair("buyer_id", buyerId));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                res = EntityUtils.toString(resEntity);
                // .....Read the response
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;

    }


    public String chatUserMessageList(String userId, String latitude,
                                      String longitude, String url) {

        Log.e("loggedin_user_id", userId);
        Log.e("latitude", latitude);
        Log.e("longitude", longitude);

        Log.e("url", url);
        // http://services.appliconsultants.com/inffo/web/service/forgetpassword?email=aksinha1982@gmail.com
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), 10000); // Timeout
            HttpPost httppost = new HttpPost(url);
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            nameValuePairs.add(new BasicNameValuePair("loggedin_user_id",
                    userId));
            nameValuePairs.add(new BasicNameValuePair("latitude", latitude));
            nameValuePairs.add(new BasicNameValuePair("longitude", longitude));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                res = EntityUtils.toString(resEntity);
                // .....Read the response
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;

    }

    public String chatList(String userId, String sender_id, String url) {

        Log.e("loggedin_user_id", userId);
        Log.e("sender_id", sender_id);

        Log.e("url", url);
        // http://services.appliconsultants.com/inffo/web/service/forgetpassword?email=aksinha1982@gmail.com
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), 10000); // Timeout
            HttpPost httppost = new HttpPost(url);
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("loggedin_user_id",
                    userId));
            nameValuePairs.add(new BasicNameValuePair("sender_id", sender_id));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                res = EntityUtils.toString(resEntity);
                // .....Read the response
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;

    }

    public String sendMessage(String sender_id, String receiver_id,
                              String message, String url) {

        Log.e("sender_id", sender_id);
        Log.e("receiver_id", receiver_id);
        Log.e("message", message);

        Log.e("url", url);
        // http://services.appliconsultants.com/inffo/web/service/forgetpassword?email=aksinha1982@gmail.com
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), 10000); // Timeout
            HttpPost httppost = new HttpPost(url);
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            nameValuePairs.add(new BasicNameValuePair("sender_id", sender_id));
            nameValuePairs.add(new BasicNameValuePair("receiver_id",
                    receiver_id));
            nameValuePairs.add(new BasicNameValuePair("message", message));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                res = EntityUtils.toString(resEntity);
                // .....Read the response
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;

    }

    public String myCommentList(String userId, String url) {

        Log.e("loggedin_user_id", userId);

        Log.e("url", url);
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), 10000); // Timeout
            HttpPost httppost = new HttpPost(url);
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("loggedin_user_id",
                    userId));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                res = EntityUtils.toString(resEntity);
                // .....Read the response
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;

    }

    public String postComment(String userId, String institute_id,
                              String comment, String ratting, String url) {

        Log.e("loggedin_user_id", userId);
        Log.e("institute_id", institute_id);
        Log.e("comment", comment);
        Log.e("ratting", ratting);

        Log.e("url", url);
        // loggedin_user_id=2&institute_id=1&comment=Good%20institute&ratting=4
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), 10000); // Timeout
            HttpPost httppost = new HttpPost(url);
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
            nameValuePairs.add(new BasicNameValuePair("loggedin_user_id",
                    userId));
            nameValuePairs.add(new BasicNameValuePair("institute_id",
                    institute_id));
            nameValuePairs.add(new BasicNameValuePair("comment", comment));
            nameValuePairs.add(new BasicNameValuePair("ratting", ratting));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                res = EntityUtils.toString(resEntity);
                // .....Read the response
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;

    }

    public String myProfile(String userId, String url) {

        Log.e("loggedin_user_id", userId);

        Log.e("url", url);
        // http://services.appliconsultants.com/inffo/web/service/forgetpassword?email=aksinha1982@gmail.com
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), 10000); // Timeout
            HttpPost httppost = new HttpPost(url);
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("loggedin_user_id",
                    userId));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                res = EntityUtils.toString(resEntity);
                // .....Read the response
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;

    }

    public String changeSettings(String userId, String field_name,
                                 String field_value, String url) {

        Log.e("loggedin_user_id", userId);
        Log.e("field_name", field_name);
        Log.e("field_value", field_value);

        Log.e("url", url);
        // http://services.appliconsultants.com/inffo/web/service/forgetpassword?email=aksinha1982@gmail.com
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), 10000); // Timeout
            HttpPost httppost = new HttpPost(url);
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            nameValuePairs.add(new BasicNameValuePair("loggedin_user_id",
                    userId));
            nameValuePairs
                    .add(new BasicNameValuePair("field_name", field_name));
            nameValuePairs.add(new BasicNameValuePair("field_value",
                    field_value));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                res = EntityUtils.toString(resEntity);
                // .....Read the response
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;

    }

    public String updateProfile(String user_id, String name, String age,
                                String location, String latitude, String logitude,
                                String profile_pic, String url) throws ClientProtocolException,
            IOException {
        // field_name can be Name or ProfilePic
        // Parameter for image is : profile_pic
        /***/
        String responce = "";
        HttpClient httpclient = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), 10000); // Timeout
        HttpPost httppost = new HttpPost(url);
        FileBody filebodyImage = null;
        if (!profile_pic.equalsIgnoreCase("")) {
            filebodyImage = new FileBody(new File(profile_pic));
        }
        Log.e("user_id", user_id);
        Log.e("name", name);
        Log.e("age", age);
        Log.e("location", location);
        Log.e("latitude", latitude);
        Log.e("logitude", logitude);
        Log.e("profile_pic", profile_pic);
        Log.e("url", url);

        StringBody userId1 = new StringBody(user_id);
        StringBody name1 = new StringBody(name);
        StringBody age1 = new StringBody(age);
        StringBody location1 = new StringBody(location);
        StringBody latitude1 = new StringBody(latitude);
        StringBody longitude1 = new StringBody(logitude);

        MultipartEntity reqEntity = new MultipartEntity();
        if (!profile_pic.equalsIgnoreCase("")) {
            reqEntity.addPart("profile_pic", filebodyImage);
        }
        reqEntity.addPart("user_id", userId1);
        reqEntity.addPart("name", name1);
        reqEntity.addPart("age", age1);
        reqEntity.addPart("location", location1);
        reqEntity.addPart("latitude", latitude1);
        reqEntity.addPart("logitude", longitude1);

        httppost.setEntity(reqEntity);

        // DEBUG
        System.out.println("executing request " + httppost.getRequestLine());
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity resEntity = response.getEntity();

        // DEBUG
        System.out.println(response.getStatusLine());
        if (resEntity != null) {
            responce = EntityUtils.toString(resEntity);
            System.out.println(responce);
        } // end if
        // Log.e("reponse", "****************"+EntityUtils.toString(
        // resEntity ));
        if (resEntity != null) {
            resEntity.consumeContent();
        } // end if

        httpclient.getConnectionManager().shutdown();

        return responce;

        /***/

    }


    public String localNotifications(String sellerId, String url) {

        Log.e("seller_id-->", sellerId);
        Log.e("url", url);

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), 10000); // Timeout
            HttpPost httppost = new HttpPost(url);
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("seller_id", sellerId));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                res = EntityUtils.toString(resEntity);
                // .....Read the response
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;
    }


}
