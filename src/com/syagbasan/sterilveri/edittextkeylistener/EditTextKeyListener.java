package com.syagbasan.sterilveri.edittextkeylistener;

import android.text.InputType;
import android.text.Spanned;
import android.text.method.NumberKeyListener;


public class EditTextKeyListener extends NumberKeyListener {

	private char[] mAccepted;
	private static EditTextKeyListener sInstance;
	public static boolean userinputlistenerpointexception = false; 
	String[] splits = null;
	
	@Override
	protected char[] getAcceptedChars() {
	    return mAccepted;
	}
	
	private static final char[] CHARACTERS =
	
	new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.' };
	
	private EditTextKeyListener() {
	    mAccepted = CHARACTERS;    
	}
	
	public static EditTextKeyListener getInstance() {
	    if (sInstance != null)
	        return sInstance;
	
	    sInstance = new EditTextKeyListener();
	    return sInstance;
	}
	
	public int getInputType() {
	    return InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL;
	}
	
	@Override
	public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
	 
	        String destTxt = dest.toString();
	        String resultingTxt = destTxt.substring(0, dstart)
	                + source.subSequence(start, end)
	                + destTxt.substring(dend);
	        if (!resultingTxt.matches("^\\d{1,9}(\\.(\\d{1,2}?)?)?")) {
	            return "";
	        } else {
	        	
	        	splits = resultingTxt.split("\\.");
	        	if (splits.length == 1) {
	            	userinputlistenerpointexception = false;	
				}else{
					userinputlistenerpointexception = true;
				}
	        }
	    return null;
	}
}