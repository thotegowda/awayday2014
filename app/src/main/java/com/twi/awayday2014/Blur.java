package com.twi.awayday2014;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

public class Blur {
    private Bitmap mBitmapIn;
    private Bitmap mBitmapOut;
    private RenderScript mRS;
    private Allocation mAllocationOne;
    private Allocation mAllocationTwo;
    private ScriptIntrinsicBlur mScriptIntrinsicBlur;

    public Blur(Context context) {
        mRS = RenderScript.create(context);
        mScriptIntrinsicBlur = ScriptIntrinsicBlur.create(mRS, Element.U8_4(mRS));
    }

    public Bitmap blur(Bitmap bitmap, float blurStrength){
        setBitmapIn(bitmap);
        mScriptIntrinsicBlur.setRadius(blurStrength);
        mScriptIntrinsicBlur.setInput(mAllocationOne);
        mScriptIntrinsicBlur.forEach(mAllocationTwo);
        mAllocationTwo.copyTo(mBitmapOut);
        return mBitmapOut;
    }

    private void setBitmapIn(Bitmap bitmapIn) {
        mBitmapIn = bitmapIn;
        mBitmapOut = Bitmap.createBitmap(mBitmapIn.getWidth(), mBitmapIn.getHeight(),
                mBitmapIn.getConfig());
        mAllocationOne = Allocation.createFromBitmap(mRS, mBitmapIn,
                Allocation.MipmapControl.MIPMAP_NONE,
                Allocation.USAGE_SCRIPT);
        mAllocationTwo = Allocation.createTyped(mRS, mAllocationOne.getType());
    }
}
