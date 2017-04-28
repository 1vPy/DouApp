// IMusicPlayer.aidl
package com.roy.douapp;



// Declare any non-default types here with import statements

interface IMusicPlayerListener {

        void onBufferingUpdate(in int bufferingProgress);

        void onProgressUpdate(in int position,in int duration);

        void onMusicChange(in String musicName,in String singerName,in String albumPicUrl);

}
