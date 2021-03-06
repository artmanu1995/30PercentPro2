package project.jakkit.restaurant;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by AloneBOY on 13/09/2017.
 */

public class ListCookOutActivity extends ActionBarActivity {
    private String strTableID, strNameFood, strHotLevel, strAmount, strOpenID;

    private ListView ListCookOut;
    private ShowOrderTABLE objShowOrderTABLE;
    private OrderTABLE objOrderTABLE;
    private ListOrderTABLE objListOrderTABLE;
    private FoodTABLE objFoodTABLE;

    private String strSttSendOK = "0", strDefaultSttPay = "1";

    ConnectionClass connectionClass;
    ProgressDialog progressDialog;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cookout_list);

        objShowOrderTABLE = new ShowOrderTABLE(this);
        objFoodTABLE = new FoodTABLE(this);
        objOrderTABLE = new OrderTABLE(this);
        objListOrderTABLE = new ListOrderTABLE(this);

        connectionClass = new ConnectionClass();
        progressDialog = new ProgressDialog(this);

        bindWidget();
        synJSONListOrder();
        createListViewCook();

    }
    public void synJSONListOrder() {
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy objPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(objPolicy);
        }

        InputStream objInputStream = null;
        String strJSON = "";
        try {
            HttpClient objHttpClient = new DefaultHttpClient();
            HttpPost objHttpPost = new HttpPost("http://192.168.1.40/join_order_out.php");
            HttpResponse objHttpResponse = objHttpClient.execute(objHttpPost);
            HttpEntity objHttpEntity = objHttpResponse.getEntity();
            objInputStream = objHttpEntity.getContent();

        } catch (Exception e) {
            Log.d("oic", "InputStream ==> " + e.toString());
        }
        //Create strJSON
        try {
            BufferedReader objBufferedReader = new BufferedReader(new InputStreamReader(objInputStream, "UTF-8"));
            StringBuilder objStringBuilder = new StringBuilder();
            String strLine = null;

            while ((strLine = objBufferedReader.readLine()) != null) {
                objStringBuilder.append(strLine);
            }   // while
            objInputStream.close();
            strJSON = objStringBuilder.toString();
        } catch (Exception e) {
            Log.d("oic", "strJSON ==> " + e.toString());
        }
        //UpData SQLite
        try {
            final JSONArray objJsonArray = new JSONArray(strJSON);
            for (int i = 0; i < objJsonArray.length(); i++) {
                JSONObject objJSONObject = objJsonArray.getJSONObject(i);
                String strOpenID = objJSONObject.getString("order_openTable");
                String strTableID =objJSONObject.getString("table_id");
                String strFoodID = objJSONObject.getString("food_id");
                String strAmount = objJSONObject.getString("listO_amount");
                String strHotLevel = objJSONObject.getString("listO_hot");
                String strSttSend = objJSONObject.getString("sttSO_id");
                String strSttPay = objJSONObject.getString("sttPay_id");
                Integer intAmount = Integer.parseInt(strAmount);

          /*      Log.d("sho", "OpenTableID ==> " + strOpenID);
                Log.d("sho", "TableID ==> " + strTableID);
                Log.d("sho", "FoodID ==> " + strFoodID);
                Log.d("sho", "Hot ==> " + strHotLevel);
                Log.d("sho", "SttSend ==> " + strSttSend);
                Log.d("sho", "SttPay ==> " + strSttPay);
                Log.d("sho", "Amount ==> " + intAmount); */

                    if (strSttSend.equals(strSttSendOK) && strSttPay.equals(strDefaultSttPay)) {
                        try {
                            String strSynFoodResult[] = objFoodTABLE.searchFood(strFoodID);
                            strFoodID = strSynFoodResult[0];
                            String strNameFood = strSynFoodResult[1];

                       // Log.d("how", "NameFood ==> " + strNameFood);

                            long AddValue = objListOrderTABLE.addValueToListOrder(strOpenID, strTableID, strNameFood, strHotLevel, intAmount);
                        } catch (Exception e) {
                        }
                    }
                }
        } catch (Exception e) {
            Log.d("oic", "Update ==> " + e.toString());
        }
    }

    private void createListViewCook() {
        final String[] strListOpenID = objListOrderTABLE.readAllListLID();
        final String[] strListTableID = objListOrderTABLE.readAllListTableID();
        final String[] strListFood = objListOrderTABLE.readAllListNameFood();
        final String[] strListHot = objListOrderTABLE.readAllListHot();
        final String[] strListAmount = objListOrderTABLE.readAllListAmount();

        AdapterListCookOut objMyAdapter = new AdapterListCookOut(ListCookOutActivity.this, strListOpenID, strListTableID, strListFood, strListHot, strListAmount);
        ListCookOut.setAdapter(objMyAdapter);

        //Click Active
        ListCookOut.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                strOpenID = strListOpenID[position];
                strTableID = strListTableID[position];
                strNameFood = strListFood[position];
                strHotLevel = strListHot[position];
                strAmount = strListAmount[position];
            }
        });
    }
    private void bindWidget() {
        ListCookOut = (ListView)findViewById(R.id.listCookOut);
    }
}
