package com.example.chie.bulletinboard_0_5_2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;


//タイトルと本文の編集画面
//implementsでクリックリスナーをつけておく
//このActivityが動作するのは
//　１．ADDボタンが押され、新規投稿をするとき
//　２．投稿を長押し後、コンテキストメニューで編集ボタンを押した時

public class EditActivity extends AppCompatActivity implements View.OnClickListener{

    private Realm realm;
    private long boardId;
    private EditText editTextTitle;
    private EditText editTextPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        //ソフトウェアキーボードを開く
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        //OK・キャンセルボタンの設定
        editTextTitle = (EditText) findViewById(R.id.edit_text_title);
        editTextPost = (EditText) findViewById(R.id.edit_text_post);
        findViewById(R.id.button_ok).setOnClickListener(this);
        findViewById(R.id.button_cancel).setOnClickListener(this);

        initData();
    }

    private void initData(){
        realm = Realm.getDefaultInstance();

        //listIdの中身から、ADDボタンと編集ボタン、
        //どちらからEditActivityに来たかを判定
        String listId = getIntent().getStringExtra("list_id");
        if(TextUtils.isEmpty(listId))
        {//ADDボタン
            //新規の投稿なので新IDを決定
            boardId = nextBoardId();
        }
        else
        {//編集ボタン
            boardId = Long.parseLong(listId);
            Log.d("edit", "initData_boardId = " + boardId);

            //realmのIDと照らし合わせ、
            //一致するときのpostとtitleをeditTextに表示
            editTextPost.setText(realm.where(Board.class).equalTo("realmID", boardId)
                    .findFirst().getRealmPost());
            editTextPost.setSelection(editTextPost.getText().length());
            editTextTitle.setText(realm.where(Board.class).equalTo("realmID", boardId)
                    .findFirst().getRealmTitle());
            editTextTitle.setSelection(editTextTitle.getText().length());
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button_ok) insert();
        finish();
    }

    public long nextBoardId(){
        //最大realmID+1を返す
        RealmResults<Board> results = realm.where(Board.class).findAll()
                .sort("realmID", Sort.DESCENDING);
        if(results.size() > 0)
            return results.first().getRealmID() + 1;
        return 0;
    }

    public void insert(){
        realm.executeTransaction(new Realm.Transaction(){
            @Override
            public  void execute(Realm realm){
                Board board = new Board();
                board.setRealmID(boardId);
                board.setRealmTitle(editTextTitle.getText().toString());
                board.setRealmPost(editTextPost.getText().toString());
                realm.insertOrUpdate(board);
            }
        });
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        realm.close();
    }
}
