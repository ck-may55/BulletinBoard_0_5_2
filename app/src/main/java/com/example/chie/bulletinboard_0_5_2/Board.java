package com.example.chie.bulletinboard_0_5_2;

import android.util.Log;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by chie on 2017/04/01.
 */

public class Board extends RealmObject {

    //Realm内の操作であることを明確にするため
    //変数名の先頭に"realm"を付けた

    @PrimaryKey
    public long realmID;
    public String realmTitle;
    public String realmPost;


    public long getRealmID(){
        //Log.d("board","getIDが呼ばれた");
        return realmID;
    }

    public void setRealmID(long realmID){
        //Log.d("board","setIDが呼ばれた");
        this.realmID = realmID;
    }

    public String getRealmTitle(){
        //Log.d("board","getTitleが呼ばれた");
        return realmTitle;
    }

    public void setRealmTitle(String realmTitle){
        //Log.d("board","setTitleが呼ばれた");
        this.realmTitle = realmTitle;
    }

    public String getRealmPost() {
        //Log.d("board","getPostが呼ばれた");
        return realmPost;
    }

    public void setRealmPost(String realmPost) {
        //Log.d("board","setPostが呼ばれた");
        this.realmPost = realmPost;
    }


}