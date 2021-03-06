package com.qmul.project.challengeme;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


public abstract class ChallengeHistoryElement {
	
	private Drawable icon;
	private String text1;
	private String text2;
	private String text3;
	private int requestCode;
	
	public String getText1() {
		return text1;
	}
	public void setText1(String text1) {
		this.text1 = text1;
		if (adapter != null) {
		    adapter.notifyDataSetChanged();
		}
	}
	public String getText2() {
		return text2;
	}
	public void setText2(String text2) {
		this.text2 = text2;
		if (adapter != null) {
		    adapter.notifyDataSetChanged();
		}
	}
	
	public String getText3() {
		return text3;
	}
	public void setText3(String text3) {
		this.text3 = text3;
		if (adapter != null) {
		    adapter.notifyDataSetChanged();
		}
	}
	
	
	public Drawable getIcon() {
		return icon;
	}
	public int getRequestCode() {
		return requestCode;
	}
	public ChallengeHistoryElement(Drawable icon, String text1, String text2, String text3,
			int requestCode) {
		super();
		this.icon = icon;
		this.text1 = text1;
		this.text2 = text2;
		this.text3 = text3;
		this.requestCode = requestCode;
	}

	private BaseAdapter adapter;
	
	protected abstract View.OnClickListener getOnClickListener();
	
	public void setAdapter(BaseAdapter adapter) {
	    this.adapter = adapter;
	}
	
	
	protected void onActivityResult(Intent data) {}

	protected void onSaveInstanceState(Bundle bundle) {}

	protected boolean restoreState(Bundle savedState) {
	    return false;
	}

	protected void notifyDataChanged() {
	    adapter.notifyDataSetChanged();
	}
	
	
	
	
	
}
