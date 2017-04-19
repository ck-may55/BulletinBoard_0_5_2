package com.example.chie.bulletinboard_0_5_2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private static final String[] initPost ={
            "投稿内容が表示されます",
            "各投稿を長押しでメニューが開きます",
            "右下のADDボタンで新規投稿ができます。"
    };
    private static final String[] initTitle = {
            "件名1", "件名2", "件名3"
    };

    private ListView mListView;
    private Realm mRealm;
    private BoardAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //activity_mainのlist_viewを扱えるようにする
        mListView = (ListView) findViewById(R.id.list_view);

        //コンテキストメニューが開けるように設定
        registerForContextMenu(mListView);

        Button buttonAdd = (Button) findViewById(R.id.button_add);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                startActivity(intent);
            }
        });

        initBoards();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        if (v.getId() == R.id.list_view){
            menu.setHeaderTitle("選択アイテム");
            menu.add(0,0,0,"編集");
            menu.add(0,1,0,"削除");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //onContextItemSelectedでは
        //onItemClickListenerのようにidを引数に持たないため
        //info.id を用いて、EditActiviteに渡している(listId)

        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        long listId;
        listId = info.id;
        if(item.getItemId() == 0 ){
            Intent intent = new Intent(MainActivity.this, EditActivity.class);
            intent.putExtra("list_id", Long.toString(listId));
            startActivity(intent);
        }
        else {
            deleteBoard(listId);
        }
        return true;
    }

    private void deleteBoard(long id){
        final long boardId =id;
        mRealm.executeTransaction(new Realm.Transaction(){
            @Override
            public void execute(Realm realm){
                Board board = realm.where(Board.class).equalTo("realmID",boardId).findFirst();
                board.deleteFromRealm();
            }
        });
    }


    private void initBoards(){
        //アプリ起動直後のListViewの初期化
        //インストール直後：initPostとinitTitle
        //それ以外      　：すでにあるデータから表示
        mRealm = Realm.getDefaultInstance();
        RealmResults<Board> boards = mRealm.where(Board.class).findAll().sort("realmID");
        if(boards.size() == 0){
            mRealm.beginTransaction();
            for(int i=0; i<initPost.length; i++){
                Board board = mRealm.createObject(Board.class, i);
                board.setRealmTitle(initTitle[i]);
                board.setRealmPost(initPost[i]);
            }
            mRealm.commitTransaction();
        }
        mAdapter = new BoardAdapter(this, boards);
        mListView.setAdapter(mAdapter);
    }

    protected void onDestroy(){
        super.onDestroy();
        mRealm.close();
    }
}
