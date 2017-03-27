package com.app.stryker;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.jsoncall.JsonCall;
import com.app.listeners.IContactsCount;
import com.app.listeners.ICountCallBack;
import com.app.model.ContactModel;
import com.app.model.ContactModelNew;
import com.app.utill.AppUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;

public class Contact extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,IContactsCount {


    private static final String VERIFY_CONTACT = "http://marketapp.online/web/salesline/api/usersearch";
    private static final String INVITE_USER = "http://marketapp.online/web/salesline/api/sentmessage";
    private static final int MY_SOCKET_TIMEOUT_MS = 50000;
    TextView nocontacttv;
    ListView listView;
    LinearLayout buttonInvite;

    List<ContactModel> contactModelsList;
    Toolbar toolbar;
    ImageView imageViewback;
    ArrayList<ContactModelNew> tempArrayList;
    StringBuilder number;
    SharedPreferences sharedPreferences;
    ArrayList<ContactModelNew> arrayListNewContact;
    SwipeRefreshLayout swipeRefreshLayout;
    private boolean check = true;
    private String sponserId = null;
    TextView textView;
    int i =0;
    int limit;
    private ProgressDialog progressDialog1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.black));
        }

        setContentView(R.layout.content_contact);


        if (getIntent().hasExtra("sponserId")) {
            sponserId = getIntent().getStringExtra("sponserId");
        }

        if (getIntent().hasExtra("limit")) {
            limit =getIntent().getIntExtra("limit",5);
        }

        sharedPreferences = getSharedPreferences("Contact", MODE_PRIVATE);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        nocontacttv = (TextView) findViewById(R.id.numbercontact);
        listView = (ListView) findViewById(R.id.listcontact);
        buttonInvite = (LinearLayout) findViewById(R.id.invite);
        imageViewback = (ImageView) findViewById(R.id.back);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.pull_to);

        textView = (TextView) findViewById(R.id.count);
        swipeRefreshLayout.setOnRefreshListener(this);
       /* swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);*/
        //arrayListNewContact = sqLiteHelper.getAllRecordNew();
        new ContactTask().execute();


        buttonInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int count = Integer.parseInt(textView.getText().toString());

                if (count > 0) {
                    if (count > limit) {
                        Toast.makeText(Contact.this, "You have completed your invitation for this week ", Toast.LENGTH_SHORT).show();
                    } else {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 0; i < arrayListNewContact.size(); i++) {
                            if (arrayListNewContact.get(i).isChecked) {
                                String number = arrayListNewContact.get(i).getPhonNum().replace("+91","");
                                if(number.charAt(0)=='0')
                                {
                                    number =  number.substring(1,number.length());
                                }
                                stringBuilder.append(arrayListNewContact.get(i).getName() + "_" + number.trim() + ",");
                            }
                        }

                        sendInvitiation(stringBuilder);
                    }

                } else
                    Toast.makeText(Contact.this, "No Contact Selected", Toast.LENGTH_SHORT).show();

            }
        });

        imageViewback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void sendInvitiation(final StringBuilder strings) {

        final ProgressDialog progressDialog = new ProgressDialog(Contact.this);
        progressDialog.setMessage("Sending Invitation");
        progressDialog.setCancelable(false);
        progressDialog.show();


        progressDialog.setMessage("Sending Invitation");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, INVITE_USER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.i("responseInvite", response);
                        Toast.makeText(Contact.this, "Invitation sent", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Contact.this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Contact.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                String val = new String(strings);
                val = val.substring(0, val.length() - 1);
                if (sponserId == null) {
                    params.put("userid", AppUtil.getUserId(Contact.this));
                    params.put("flag", "1");

                } else {
                    params.put("userid", sponserId);
                    params.put("flag", "0");
                }

                params.put("mobileno", val);

                return params;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                int textlength = newText.length();

                tempArrayList = new ArrayList<>();
                for (ContactModelNew c : arrayListNewContact) {
                    if (textlength <= c.getName().length()) {
                        if (c.getName().toLowerCase().contains(newText.toString().toLowerCase())) {
                            tempArrayList.add(c);
                        }
                    }
                }
                Log.i("temp size", tempArrayList.size() + "");
                MyArrayAdapterContact myArrayAdapterContact = new MyArrayAdapterContact(getApplicationContext(), R.layout.row_contact, tempArrayList);

                myArrayAdapterContact.icallBack = new ICountCallBack() {
                    @Override
                    public void onCount(int count) {

                        textView = (TextView) findViewById(R.id.count);

                        textView.setText(count + "");
                    }
                };
                listView.setAdapter(myArrayAdapterContact);


                return true;
            }
        });
        return true;
    }

    public List<ContactModel> getContacts(Context ctx,ProgressDialog progressDialog,TextView textView) {
        List<ContactModel> list = new ArrayList<>();
        ContentResolver contentResolver = ctx.getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if (cursor.getCount() > 0) {

            while (cursor.moveToNext()) {
                i++;
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor cursorInfo = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                    InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(ctx.getContentResolver(),
                            ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(id)));

                    Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(id));
                    Uri pURI = Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);

                    Bitmap photo = null;
                    if (inputStream != null) {
                        photo = BitmapFactory.decodeStream(inputStream);
                    }
                    while (cursorInfo.moveToNext()) {
                        ContactModel info = new ContactModel();
                        info.setId(id);
                        info.setName(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                        info.setMobileNumber(cursorInfo.getString(cursorInfo.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                        info.setPhoto(photo);
                        info.setPhotoURI(pURI);
                        list.add(info);

                    }
                    progressDialog.setProgress(i);
                  //  textView.setText(i+"");
                    //    nocontacttv.setText(progressDialog1.getMax()+"");
                 /* new Thread(new Runnable() {
                      @Override
                      public void run() {
                          iContactsCount.onCount(i+"");
                      }
                  }).start();*/

                  /* new Thread(new Runnable() {
                       @Override
                       public void run() {
                           nocontacttv.setText(i+"");
                       }
                   }).start();*/
                    cursorInfo.close();
                }
            }
        }
        return list;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onRefresh() {
        check = false;
        swipeRefreshLayout.setRefreshing(true);

        refreshList();
    }

    private void refreshList() {
        new ContactTask().execute();
    }

    @Override
    public void onCount(String count) {
        nocontacttv.setText(count+"");

    }


    private class ContactTask extends AsyncTask<Void, Void, Long> {



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog1 = new ProgressDialog(Contact.this);
            progressDialog1.setMessage("Please Wait...");
            progressDialog1.setCancelable(false);
            progressDialog1.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            ContentResolver contentResolver = getApplicationContext().getContentResolver();
            Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            progressDialog1.setMax(cursor.getCount());
            if (check)
                progressDialog1.show();
        }

        @Override
        protected Long doInBackground(Void... params) {
            contactModelsList = getContacts(Contact.this,progressDialog1, nocontacttv);
            progressDialog1.dismiss();
            //  SQLiteHelper sqLiteHelper = SQLiteHelper.getInstance(Contact.this);
            long res = 9;
            /*for(int i=0;i<contactModelsList.size();i++)
            {
                res = sqLiteHelper.insertRecordOriginal(contactModelsList.get(i));
            }*/
            return res;
        }

        @Override
        protected void onPostExecute(Long aVoid) {
            super.onPostExecute(aVoid);
           // progressDialog.dismiss();
            // Toast.makeText(getApplicationContext(),"res"+aVoid,Toast.LENGTH_SHORT).show();
            nocontacttv.setText(contactModelsList.size() + "");
            number = new StringBuilder();
            for (int i = 0; i < contactModelsList.size(); i++) {
                number.append(contactModelsList.get(i).getName() + "_" + contactModelsList.get(i).getMobileNumber().replace("+91","") + ",");
            }

            verify(number);


        }
    }


    private void verify(final StringBuilder number) {
        VerifyContacts verifyContacts = new VerifyContacts();
        String val = new String(number);
        val = val.substring(0, val.length() - 1);
        verifyContacts.execute(new String[]{val});

        /* progressDialog = new ProgressDialog(Contact.this);
        progressDialog.setMessage("Verifying Contacts");
        progressDialog.setCancelable(false);
        if (check)
            progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, VERIFY_CONTACT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            progressDialog.dismiss();
                            showJson(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        swipeRefreshLayout.setRefreshing(false);
                        progressDialog.dismiss();
                        Toast.makeText(Contact.this, "Please try again", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                String val = new String(number);
                val = val.substring(0, val.length() - 1);
                params.put("user_phone", val);
                return params;
            }

        };*/
        // stringRequest.setRetryPolicy(new TokenRetryPolicy(getApplicationContext(), this));
        /*stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/

        //   RequestQueue requestQueue = Volley.newRequestQueue(this);
        //  requestQueue.add(stringRequest);
    }

    private void showJson(String response) throws Exception {

        if (response != null) {
            Log.i("reponse contact", response);

            JSONObject jsonObject = new JSONObject(response);

            String success = jsonObject.getString("success");

            if (success.equalsIgnoreCase("1")) {

                arrayListNewContact = new ArrayList<>();

                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                JSONArray jsonArray = jsonObject1.getJSONArray("mnumber_status");
                for (int i = 0; i < jsonArray.length(); i++) {
                    ContactModelNew contactModelNew = new ContactModelNew();
                    contactModelNew.setName(jsonArray.getJSONObject(i).getString("name"));
                    contactModelNew.setPhonNum(jsonArray.getJSONObject(i).getString("mobile"));
                    contactModelNew.isExist = jsonArray.getJSONObject(i).getInt("exist");


                    arrayListNewContact.add(contactModelNew);
                }

                Log.i("new Contact size", arrayListNewContact.size() + "");
                if (tempArrayList != null)
                    tempArrayList.clear();
                MyArrayAdapterContact myArrayAdapterContact = new MyArrayAdapterContact(Contact.this, R.layout.row_contact, arrayListNewContact);


                myArrayAdapterContact.icallBack = new ICountCallBack() {
                    @Override
                    public void onCount(int count) {


                        textView.setText(count + "");
                    }
                };


                listView.setAdapter(myArrayAdapterContact);

                swipeRefreshLayout.setRefreshing(false);
                MyArrayAdapterContact.count = 0;
                textView.setText(MyArrayAdapterContact.count + "");

            }
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyArrayAdapterContact.count = 0;
    }



    private class VerifyContacts extends AsyncTask<String, Void, String>
    {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(Contact.this);
            progressDialog.setMessage("verifying Contacts");
            progressDialog.setCancelable(false);
            if(check)
                progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String response = "";
            try {
                JsonCall obj = new JsonCall();
                response = obj.verifyContacts(params[0],VERIFY_CONTACT);
                Log.e("add Store RRESPONSE", "" + response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            try {
                showJson(s);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
