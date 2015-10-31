package com.syagbasan.sterilveri;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.syagbasan.sterilveri.db.OdemelerDB;
import com.syagbasan.sterilveri.edittextkeylistener.EditTextKeyListener;
import com.syagbasan.sterilveri.generaltwocontentfragmentlistviews.CustomListViewAdapterThree;
import com.syagbasan.sterilveri.generaltwocontentfragmentlistviews.RowItemThree;
import com.syagbasan.sterilveri.odemelerfragmentlistviews.CustomListViewAdapter;
import com.syagbasan.sterilveri.odemelerfragmentlistviews.RowItem;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class OdemelerFragment extends Fragment {

	Button bVeriEkle,bRemoveAllData;
	TextView tvToplamMasraf;
	
	ListView lvPersonal;
	public static List<RowItem> PersonalrowItems;
	static CustomListViewAdapter adapter;

	View rootView;
	Spinner spinner;
	
	OdemelerDB entryOdemelerDB;
	String[] duplicateddataPerson;
	
	SoundPool soundPool;
	int positiveSound,negativeSound;
	boolean loaded = false;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.odemeler_fragment, container, false);
		
		soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
		@Override
		public void onLoadComplete(SoundPool soundPool, int sampleId,int status) {
				loaded = true;
			}
		});
		positiveSound = soundPool.load(getActivity(), R.raw.positive, 1);
		negativeSound = soundPool.load(getActivity(),R.raw.negative, 1);
		
		tvToplamMasraf = (TextView) rootView.findViewById(R.id.tvToplam);
		
		ListViewContentConfig();
		
		lvPersonal = (ListView) rootView.findViewById(R.id.lvPersonal);
		PersonalrowItems = new ArrayList<RowItem>();
		adapter = new CustomListViewAdapter(getActivity(),R.layout.odemeler_fragment_list_item, PersonalrowItems);
		lvPersonal.setAdapter(adapter);
		lvPersonal.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// TODO Auto-generated method stub
				// TODO Personal Listview Duplicated
				AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
	            //builder1.setMessage("'" + entryPersonalDB.getAllDataWhereDate(entryPersonalDB.getPersonKeyDate(adapter2.getItem(position).toString())[position]) + "'adlý veriyi silmek istediðinize emin misiniz?");
				builder1.setMessage("'"+PersonalrowItems.get(position).getDate().replace("\n", "")+" "+PersonalrowItems.get(position).getPerson()+" "+PersonalrowItems.get(position).getAmount()+"' Veriyi silmek yada düzeltmek için seçiniz... ");
				builder1.setCancelable(true);
	            builder1.setPositiveButton("Sil",
	                    new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog2, int id) {
	                	//entryPersonalDB.deleteSpesific(adapter2.getItem(position).toString());		                	
	                	AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			            //builder1.setMessage("'" + entryPersonalDB.getAllDataWhereDate(entryPersonalDB.getPersonKeyDate(adapter2.getItem(position).toString())[position]) + "'adlý veriyi silmek istediðinize emin misiniz?");
	                	builder.setMessage("'"+PersonalrowItems.get(position).getDate().replace("\n", "")+" "+PersonalrowItems.get(position).getPerson()+" "+PersonalrowItems.get(position).getAmount()+"' Veriyi silmek istediðinize emin misiniz?");
	                	builder.setCancelable(true);
	                	builder.setPositiveButton("Evet",
			                    new DialogInterface.OnClickListener() {
			                public void onClick(DialogInterface dialog23, int id) {
			                	//entryPersonalDB.deleteSpesific(adapter2.getItem(position).toString());	
			                	String[] parts = PersonalrowItems.get(position).getAmount().split(" ");
								String number = parts[0];
								if (number.contains(".")&&number.contains(",")) {
									number = number.replace(".", "");
									number = number.replace(",", ".");
								}else if (number.contains(".")&&!number.contains(",")) {
									number = number.replace(".", "");
								}else if (!number.contains(".")&&number.contains(",")) {
									number = number.replace(",", ".");
								}
								boolean returned = false;
								for (int i = 0; i < entryOdemelerDB.getPersonKeyAllRowId(PersonalrowItems.get(position).getDate(),PersonalrowItems.get(position).getPerson(),number).length; i++) {
									if (entryOdemelerDB.getPersonKeyAllRowId(PersonalrowItems.get(position).getDate(),PersonalrowItems.get(position).getPerson(),number)[i] != null) {
										returned = entryOdemelerDB.deleteToDo(Long.parseLong(entryOdemelerDB.getPersonKeyAllRowId(PersonalrowItems.get(position).getDate(),PersonalrowItems.get(position).getPerson(),number)[i]));
									}
								}
			                	
			                	if (returned) {
			                		Toast.makeText(getActivity(), "Silindi", Toast.LENGTH_SHORT).show();
			                		if (loaded) {
										soundPool.play(negativeSound,  0.5f,0.5f, 1, 0, 1f);
									}
			                		
			                		RefreshPersonalList();
				                	dialog23.dismiss();
			                	}	
			                }
			            });
			            
	                	builder.setNegativeButton("Hayýr",
			                    new DialogInterface.OnClickListener() {
			                public void onClick(DialogInterface dialog33, int id) {
			                    dialog33.cancel();
			                }
			            });

			            AlertDialog alert = builder.create();
			            alert.show();
	                }
	            });
	            
	            builder1.setNeutralButton("Düzelt", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog15, int which) {
						// TODO Düzelt
						dialog15.dismiss();
						String[] parts = PersonalrowItems.get(position).getAmount().split(" ");
						String number = parts[0];
						if (number.contains(".")&&number.contains(",")) {
							number = number.replace(".", "");
							number = number.replace(",", ".");
						}else if (number.contains(".")&&!number.contains(",")) {
							number = number.replace(".", "");
						}else if (!number.contains(".")&&number.contains(",")) {
							number = number.replace(",", ".");
						}
						
						UpdateRow(Long.parseLong(entryOdemelerDB.getPersonKeyAllRowId(PersonalrowItems.get(position).getDate(),PersonalrowItems.get(position).getPerson(),number)[0]),PersonalrowItems.get(position).getDate(),PersonalrowItems.get(position).getPerson(),number);
						RefreshPersonalList();
					}
				});
	            
	            builder1.setNegativeButton("Vazgeç",
	                    new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog2, int id) {
	                    dialog2.cancel();
	                }
	            });

	            AlertDialog alert11 = builder1.create();
	            alert11.show();
				return true;
			}
		});
		lvPersonal.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				if (lvPersonal.getOnItemLongClickListener() != null) {
					Toast.makeText(getActivity(), "'"+PersonalrowItems.get(position).getDate().replace("\n", "")+" "+PersonalrowItems.get(position).getPerson()+" "+PersonalrowItems.get(position).getAmount()+"' Veriyi silmek yada düzeltmek için uzun týklayýnýz...", Toast.LENGTH_LONG).show();	
				}
			}
		});				
		
		bVeriEkle = (Button) rootView.findViewById(R.id.bVeriEkle);
		bVeriEkle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Kisi Ekle			
				final Dialog dialog;
				dialog = new Dialog(rootView.getContext(),android.R.style.Theme_Translucent);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setCancelable(true);
				dialog.setContentView(R.layout.odemeler_add);
				dialog.show();				

			    ArrayAdapter<String> mAdapter;
			    spinner = (Spinner) dialog.findViewById(R.id.spinner1);
			    mAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, duplicateddataPerson);
				spinner.setAdapter(mAdapter);
				
				
				final LinearLayout llPersonLayout = (LinearLayout) dialog.findViewById(R.id.llPersonLayout);
				final LinearLayout llPersonSpinnerLayout = (LinearLayout) dialog.findViewById(R.id.llPersonSpinnerLayout);
				if (entryOdemelerDB.getPerson().length!=0) {
					llPersonLayout.setVisibility(View.GONE);
					llPersonSpinnerLayout.setVisibility(View.VISIBLE);
				}else{
					llPersonLayout.setVisibility(View.VISIBLE);
					llPersonSpinnerLayout.setVisibility(View.GONE);
				}
			
				final EditText etPerson = (EditText) dialog.findViewById(R.id.etPerson);
				final EditText etAmount = (EditText) dialog.findViewById(R.id.etA);
				etAmount.setKeyListener(EditTextKeyListener.getInstance());
				
				final CheckBox cbNewPersonAdd = (CheckBox) dialog.findViewById(R.id.cbNewPersonAdd);
				if (spinner.getCount()==0) {
					cbNewPersonAdd.setChecked(true);
					cbNewPersonAdd.setClickable(false);
				}
				cbNewPersonAdd.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						if (isChecked) {
							llPersonLayout.setVisibility(View.VISIBLE);
							llPersonSpinnerLayout.setVisibility(View.GONE);
						}else{
							llPersonLayout.setVisibility(View.GONE);
							llPersonSpinnerLayout.setVisibility(View.VISIBLE);
						}
					}
				});
				
				final LinearLayout llDateAdd = (LinearLayout) dialog.findViewById(R.id.llTarihEkle);
				llDateAdd.setVisibility(View.GONE);
				final CheckBox cbDateAdd = (CheckBox) dialog.findViewById(R.id.cbTarihEkle);
				cbDateAdd.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						// TODO Auto-generated method stub
						if (isChecked) {
							llDateAdd.setVisibility(View.VISIBLE);
						}else{
							llDateAdd.setVisibility(View.GONE);
						}
					}
				});
				final DatePicker datePicker = (DatePicker) dialog.findViewById(R.id.datePicker);
				
				Button bOk = (Button) dialog.findViewById(R.id.bOk);
				bOk.setOnClickListener(new OnClickListener() {
					
					@SuppressLint("SimpleDateFormat")
					@Override
					public void onClick(View v) {
						// TODO Veri Giris Onayi
						if (((etPerson.length()!=0 && cbNewPersonAdd.isChecked()) && etAmount.length()!=0) || (!cbNewPersonAdd.isChecked()&& spinner.getCount() != 0 && etAmount.length()!=0)) {
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
							String currentDateandTime="";
							if (cbDateAdd.isChecked()) {
								String formattedMonth = String.format(Locale.getDefault(),"%02d", (datePicker.getMonth()+1));
								String formattedDayOfMonth = String.format(Locale.getDefault(),"%02d", (datePicker.getDayOfMonth()));
								currentDateandTime = String.valueOf(datePicker.getYear()+"."+formattedMonth+"."+formattedDayOfMonth+" \n-");
							}else{
								currentDateandTime = sdf.format(new Date());
							}
							long result;
							if (cbNewPersonAdd.isChecked()) {
								result =  entryOdemelerDB.createEntry(currentDateandTime, etPerson.getText().toString(), etAmount.getText().toString());
							}else{
								result = entryOdemelerDB.createEntry(currentDateandTime, spinner.getSelectedItem().toString(), etAmount.getText().toString());
							}
												
							RefreshPersonalList();
							
							if (result != -1) {
								if (loaded) {
									soundPool.play(positiveSound,  0.5f,0.5f, 1, 0, 1f);
								}
								Toast.makeText(getActivity(), "Giriþ Baþarýlý",Toast.LENGTH_LONG).show();
							}else{
								if (loaded) {
									soundPool.play(negativeSound,  0.5f,0.5f, 1, 0, 1f);
								}
								Toast.makeText(getActivity(), "Giriþ Baþarýsýz",Toast.LENGTH_LONG).show();
							}
							dialog.dismiss();	

						}else{
							Toast.makeText(getActivity(), "Lütfen VeriLeri Giriniz...",Toast.LENGTH_LONG).show();
						}
					}
				});
			}
		});
	
		bRemoveAllData = (Button) rootView.findViewById(R.id.bRemoveAllData);
		bRemoveAllData.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Remove All Data
				if (!PersonalrowItems.isEmpty()) {
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
					builder.setMessage("Tüm Ödemeler verilerini silmek istediðinize emin misiniz?");
					builder.setCancelable(true);
					builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
		                public void onClick(DialogInterface dialog2, int id) {
		                	entryOdemelerDB.deleteAllData();
		                	Toast.makeText(getActivity(), "Tüm Ödemeler Verileri Silindi", Toast.LENGTH_SHORT).show();
	                		if (loaded) {
								soundPool.play(negativeSound,  0.5f,0.5f, 1, 0, 1f);
							}
		    				RefreshPersonalList();
			                dialog2.dismiss();
		                }
		            });
					builder.setNegativeButton("Hayýr",  new DialogInterface.OnClickListener() {
		                public void onClick(DialogInterface dialog2, int id) {
		                    dialog2.dismiss();
		                }
		            });
	
		            AlertDialog alertDialog = builder.create();
		            alertDialog.show();
				}else{
					Toast.makeText(getActivity(), "Silinebilecek herhangi bir veri mevcut deðil", Toast.LENGTH_SHORT).show();
				}
			}
		});
		return rootView;
	}
	
	private void ListViewContentConfig() {
		// TODO ListViewContentConfig
		ListView lvContent;
		List<RowItemThree> ContentrowItems;
		CustomListViewAdapterThree contentAdapter;
		lvContent = (ListView) rootView.findViewById(R.id.lvContent);
		ContentrowItems = new ArrayList<RowItemThree>();
		contentAdapter = new CustomListViewAdapterThree(getActivity(),R.layout.content_list_item3, ContentrowItems);
		lvContent.setAdapter(contentAdapter);
		contentAdapter.clear();
		RowItemThree item2 = new RowItemThree("Tarih","Ödeme Adý", "Ödeme Miktarý" );		
		ContentrowItems.add(item2);
		contentAdapter.notifyDataSetChanged();
	}
	
	@SuppressLint("SimpleDateFormat")
	public void RefreshPersonalList(){
		adapter.clear();
		String[] dataPerson = entryOdemelerDB.getPerson();
		String[] dataDate = entryOdemelerDB.getDate();
		String[] dataAmount = entryOdemelerDB.getAmount();

		List<String> list = Arrays.asList(dataPerson);
		Set<String> set = new HashSet<String>(list);
		duplicateddataPerson= new String[set.size()];
		set.toArray(duplicateddataPerson);	
		
		DecimalFormat format = new DecimalFormat();

		for (int i = 0 ; i < dataPerson.length ; i++) {			
			RowItem item = new RowItem(dataDate[i],dataPerson[i], format.format(Double.valueOf(dataAmount[i]))+" TL" );	
			PersonalrowItems.add(item);
		 }
		adapter.notifyDataSetChanged();
		double sumAmount = 0;
		for (int i = 0 ; i < dataAmount.length ; i++) {	
			if (dataAmount[i] != null) {
				sumAmount = sumAmount + Double.valueOf(dataAmount[i]);	
			}		
		}
		tvToplamMasraf.setText("Toplam: "+String.valueOf(format.format(sumAmount))+" TL");
	}
	
	@Override
	public void onResume() {
		// TODO OnResume
		super.onResume();
		entryOdemelerDB = new OdemelerDB(getActivity());
		entryOdemelerDB.open();
		RefreshPersonalList();
	};
	
	@Override
	public void onPause() {
		// TODO OnPause
		super.onPause();
		entryOdemelerDB.close();
	}

	@Override
	public void onDestroy() {
		// TODO OnDestroy
		super.onDestroy();
		entryOdemelerDB.close();
	}
	
	private void UpdateRow(final long RowID,String date,String person,String amount) {
		// TODO Auto-generated method stub
		final Dialog dialog;
		dialog = new Dialog(rootView.getContext(),android.R.style.Theme_Translucent);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(true);
		dialog.setContentView(R.layout.odemeler_fix);
		dialog.show();					
		
		final TextView tvFix = (TextView) dialog.findViewById(R.id.tvFixit);
		tvFix.setText("'"+date.replace("\n","")+" "+person+" "+amount+"' Veriler düzeltiliyor...");
		final EditText etPerson = (EditText) dialog.findViewById(R.id.etPerson);
		etPerson.setText(person);
		final EditText etAmount = (EditText) dialog.findViewById(R.id.etA);
		etAmount.setText(amount);
		etAmount.setKeyListener(EditTextKeyListener.getInstance());

		final DatePicker datePicker = (DatePicker) dialog.findViewById(R.id.datePicker);
		String[] parts = date.split(" ");
		String dateee = parts[0];
		String year = dateee.substring(0, 4);
		String month = dateee.substring(5, 7);
		String day = dateee.substring(8, 10);
	
		//Toast.makeText(getActivity(),day+month+year, Toast.LENGTH_LONG).show();
		datePicker.updateDate(Integer.valueOf(year), Integer.valueOf(month)-1, Integer.valueOf(day));
		
		Button bOk = (Button) dialog.findViewById(R.id.bOk);
		bOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Veri Giris Onayi
				if (((etPerson.length()!=0  && etAmount.length()!=0) || ( spinner.getCount() != 0 && etAmount.length()!=0))) {
					
					String currentDateandTime="";
					String formattedMonth = String.format(Locale.getDefault(),"%02d", (datePicker.getMonth()+1));
					String formattedDayOfMonth = String.format(Locale.getDefault(),"%02d", (datePicker.getDayOfMonth()));
					currentDateandTime = String.valueOf(datePicker.getYear()+"."+formattedMonth+"."+formattedDayOfMonth+" \n-");
					
					long result;
				
					result = entryOdemelerDB.updateEntry(currentDateandTime, etPerson.getText().toString(), etAmount.getText().toString(),String.valueOf(RowID));
									
					RefreshPersonalList();
				
					
					if (result != -1) {
						if (loaded) {
							soundPool.play(positiveSound,  0.5f,0.5f, 1, 0, 1f);
						}
						Toast.makeText(getActivity(), "Güncelleme Baþarýlý",Toast.LENGTH_LONG).show();
					}else{
						if (loaded) {
							soundPool.play(negativeSound,  0.5f,0.5f, 1, 0, 1f);
						}
						Toast.makeText(getActivity(), "Güncelleme Baþarýsýz",Toast.LENGTH_LONG).show();
					}
					dialog.dismiss();
					
					
				}else{
					Toast.makeText(getActivity(), "Lütfen VeriLeri Giriniz...",Toast.LENGTH_LONG).show();
				}
			}
		});
	}
}
