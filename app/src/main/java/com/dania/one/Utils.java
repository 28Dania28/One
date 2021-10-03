package com.dania.one;

import android.content.Context;

public class Utils {
    public static int getDP(float toDP, Context aContext){
        if (toDP == 0){
            return 0;
        } else{
            float density = aContext.getResources().getDisplayMetrics().density;
            return (int) Math.ceil((density * toDP));
        }
    }
}
