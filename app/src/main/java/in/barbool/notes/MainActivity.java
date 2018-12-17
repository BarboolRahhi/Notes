package in.barbool.notes;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DBManager dbManager;
    //MyCustomAdapter myadapter;
    FloatingActionButton floatingActionButton;
    ArrayList<AdapterItems> listnewsData = new ArrayList<AdapterItems>();

    RelativeLayout noItem;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        dbManager = new DBManager(this);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.addNotes);
        noItem = (RelativeLayout) findViewById(R.id.noitem);


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AddNotes.class);
                startActivity(intent);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadData();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadData();
    }

    private void loadData() {
        listnewsData.clear();
        Cursor cursor = dbManager.query(null,null,null,DBManager.ColUserName);

        if (cursor.moveToFirst()){
            String tableData="";
            do {

                listnewsData.add(new AdapterItems(cursor.getString(cursor.getColumnIndex(DBManager.ColID)),
                        cursor.getString(cursor.getColumnIndex(DBManager.ColUserName)),
                        cursor.getString(cursor.getColumnIndex(DBManager.ColPassWord)),
                        cursor.getString(cursor.getColumnIndex(DBManager.ColDate))));

            }while (cursor.moveToNext());
        }

        adapter = new CustomAdapter(listnewsData, this);
        recyclerView.setAdapter(adapter);

        int count = cursor.getCount();
        if (count>0){
            recyclerView.setVisibility(View.VISIBLE);
            noItem.setVisibility(View.INVISIBLE);
            //Toast.makeText(this, "Data is in table", Toast.LENGTH_SHORT).show();
        }else{
            recyclerView.setVisibility(View.INVISIBLE);
            noItem.setVisibility(View.VISIBLE);
           // Toast.makeText(this, "Table is empty", Toast.LENGTH_SHORT).show();
        }



//        myadapter=new MyCustomAdapter(listnewsData);
//        ListView  lsNews=(ListView)findViewById(R.id.listView);
//        lsNews.setAdapter(myadapter);
    }

//for listView adapter

//    private class MyCustomAdapter extends BaseAdapter {
//
//        public ArrayList<AdapterItems> listnewsDataAdpater ;
//
//        public MyCustomAdapter(ArrayList<AdapterItems>  listnewsDataAdpater) {
//            this.listnewsDataAdpater=listnewsDataAdpater;
//        }
//
//
//        @Override
//        public int getCount() {
//            return listnewsDataAdpater.size();
//        }
//
//        @Override
//        public String getItem(int position) {
//            return null;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent)
//        {
//            LayoutInflater mInflater = getLayoutInflater();
//            View myView = mInflater.inflate(R.layout.item_view, null);
//
//            final   AdapterItems s = listnewsDataAdpater.get(position);
//
//            TextView mtitle=(TextView)myView.findViewById(R.id.title);
//            mtitle.setText(s.Title);
//
//            //TextView mID=(TextView)myView.findViewById(R.id.id);
//            //mID.setText(s.ID);
//
//            TextView mAbout=(TextView)myView.findViewById(R.id.about);
//            mAbout.setText(s.About);
//
//            TextView mDate=(TextView)myView.findViewById(R.id.date);
//            mDate.setText(s.Date);
//
//
//
//            return myView;
//        }
//
//    }

    public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder>{

        public ArrayList<AdapterItems> listnewsDataAdpater ;
        public Context context;

        public CustomAdapter(ArrayList<AdapterItems> listnewsDataAdpater, Context context) {
            this.listnewsDataAdpater = listnewsDataAdpater;
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_view, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
            final AdapterItems s = listnewsDataAdpater.get(i);
            viewHolder.mtitle.setText(s.Title);
            viewHolder.mAbout.setText(s.About);
            viewHolder.mDate.setText(s.Date);

            viewHolder.menuBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    PopupMenu popup = new PopupMenu(context, viewHolder.menuBtn);
                    //inflating menu from xml resource
                    popup.inflate(R.menu.option_menu);
                    //adding click listener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.menu1:
                                    //handle menu1 click
                                    break;
                                case R.id.menu2:

                                    String[] SelectionArgs={s.ID};

                                    int count =  dbManager.Delete("ID=?",SelectionArgs);
                                      if (count>0)
                                      {
                                        loadData();
                                      }

                                    break;
                                case R.id.menu3:
                                    //handle menu3 click
                                    break;
                            }
                            return false;
                        }
                    });
                    //displaying the popup
                    popup.show();

                }
            });



        }

        @Override
        public int getItemCount() {
            return listnewsDataAdpater.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView mtitle;
            public TextView mAbout;
            public TextView mDate;
            public ImageView menuBtn;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                 mtitle =(TextView) itemView.findViewById(R.id.title);

                mAbout =(TextView) itemView.findViewById(R.id.about);

                mDate =(TextView) itemView.findViewById(R.id.date);
                menuBtn = (ImageView) itemView.findViewById(R.id.openMenu);



            }
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {

            case R.id.about:
                //your code
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void search(String s){
        String[] SelectionsArgs= {"%"+s+"%"};

        listnewsData.clear();
        Cursor cursor = dbManager.query(null,"Title like ?",SelectionsArgs,DBManager.ColUserName);

        if (cursor.moveToFirst()){
            String tableData="";
            do {

                listnewsData.add(new AdapterItems(cursor.getString(cursor.getColumnIndex(DBManager.ColID)),
                        cursor.getString(cursor.getColumnIndex(DBManager.ColUserName)),
                        cursor.getString(cursor.getColumnIndex(DBManager.ColPassWord)),
                        cursor.getString(cursor.getColumnIndex(DBManager.ColDate))));

            }while (cursor.moveToNext());
        }

        adapter = new CustomAdapter(listnewsData, this);
        recyclerView.setAdapter(adapter);

    }
}
