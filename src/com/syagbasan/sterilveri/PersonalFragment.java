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

import com.syagbasan.sterilveri.db.PersonalDB;
import com.syagbasan.sterilveri.edittextkeylistener.EditTextKeyListener;
import com.syagbasan.sterilveri.generaltwocontentfragmentlistviews.CustomListViewAdapterFour;
import com.syagbasan.sterilveri.generaltwocontentfragmentlistviews.CustomListViewAdapterTwo;
import com.syagbasan.sterilveri.generaltwocontentfragmentlistviews.RowItemFour;
import com.syagbasan.sterilveri.generaltwocontentfragmentlistviews.RowItemTwo;
import com.syagbasan.sterilveri.personalfragmentlistviews.CustomListViewAdapter;
import com.syagbasan.sterilveri.personalfragmentlistviews.CustomListViewAdapter2;
import com.syagbasan.sterilveri.personalfragmentlistviews.RowItem;
import com.syagbasan.sterilveri.personalfragmentlistviews.RowItem2;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class PersonalFragment extends Fragment  {

	Button bVeriEkle,bDetaylar,bRemoveAllData;
	TextView tvToplamMasraf;
	
	ListView lvPersonal;
	public static List<RowItem> PersonalrowItems;
	static CustomListViewAdapter adapter;
	
	ListView lvDetails;
	public static List<RowItem2> PersonalrowItems2;
	static CustomListViewAdapter2 adapter2;

	View rootView;
	Spinner spinner;
	
	PersonalDB entryPersonalDB;
	String[] duplicateddataPerson;
	Dialog dialog;
	
	SoundPool soundPool;
	int positiveSound,negativeSound;
	boolean loaded = false;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {	
		
		rootView = inflater.inflate(R.layout.personal_fragment, container, false);
		
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
		
		dialog = new Dialog(rootView.getContext(),android.R.style.Theme_Translucent);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(true);
		dialog.setContentView(R.layout.show_selected_personal_info);
		
		ListViewContentConfig();
		ListViewContentConfigDetails();
		
		lvDetails = (ListView) dialog.findViewById(R.id.lvSelectedPersonalInfo);
		PersonalrowItems2 = new ArrayList<RowItem2>();
		adapter2 = new CustomListViewAdapter2(getActivity(),R.layout.personal_fragment_list_item2, PersonalrowItems2);
		lvDetails.setAdapter(adapter2);
		
		lvPersonal = (ListView) rootView.findViewById(R.id.lvPersonal);
		PersonalrowItems = new ArrayList<RowItem>();
		adapter = new CustomListViewAdapter(getActivity(),R.layout.personal_fragment_list_item, PersonalrowItems);
		lvPersonal.setAdapter(adapter);
		lvPersonal.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position2, long id) {
				// TODO Personal Listview Duplicated				
				RefreshPersonalList2(position2);	
				
				dialog.show();	
				
				lvDetails.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view,final int position, long id) {
						// TODO Personal Listview
						AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
			            //builder1.setMessage("'" + entryPersonalDB.getAllDataWhereDate(entryPersonalDB.getPersonKeyDate(adapter2.getItem(position).toString())[position]) + "'adlý veriyi silmek istediðinize emin misiniz?");
						builder1.setMessage("'"+PersonalrowItems2.get(position).getDate().replace("\n", "")+" "+PersonalrowItems2.get(position).getPerson()+" "+PersonalrowItems2.get(position).getProduct()+" "+PersonalrowItems2.get(position).getAmount()+"' Veriyi silmek yada düzeltmek için seçiniz... ");
						builder1.setCancelable(true);
			            builder1.setPositiveButton("Sil",
			                    new DialogInterface.OnClickListener() {
			                public void onClick(DialogInterface dialog2, int id) {
			                	//entryPersonalDB.deleteSpesific(adapter2.getItem(position).toString());		                	
			                	AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
					            //builder1.setMessage("'" + entryPersonalDB.getAllDataWhereDate(entryPersonalDB.getPersonKeyDate(adapter2.getItem(position).toString())[position]) + "'adlý veriyi silmek istediðinize emin misiniz?");
			                	builder.setMessage("'"+PersonalrowItems2.get(position).getDate().replace("\n", "")+" "+PersonalrowItems2.get(position).getPerson()+" "+PersonalrowItems2.get(position).getProduct()+" "+PersonalrowItems2.get(position).getAmount()+"' Veriyi silmek istediðinize emin misiniz?");
			                	builder.setCancelable(true);
			                	builder.setPositiveButton("Evet",
					                    new DialogInterface.OnClickListener() {
					                public void onClick(DialogInterface dialog23, int id) {
					                	//entryPersonalDB.deleteSpesific(adapter2.getItem(position).toString());		                	
					                	boolean returned = entryPersonalDB.deleteToDo(Long.parseLong(entryPersonalDB.getPersonKeyPersonRowId(adapter2.getItem(position).toString())[position]));
					                	if (returned) {
					                		Toast.makeText(getActivity(), "Silindi", Toast.LENGTH_SHORT).show();
					                		if (loaded) {
												soundPool.play(negativeSound,  0.5f,0.5f, 1, 0, 1f);
											}
					                		
					                		RefreshPersonalList();
						                	dialog23.dismiss();
						                	
						    				if (!adapter.isEmpty()) {	
												RefreshPersonalList2(position2);
											}else{
												dialog.dismiss();
											}
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
								String[] parts = PersonalrowItems2.get(position).getAmount().split(" ");
								String number = parts[0];
								if (number.contains(".")&&number.contains(",")) {
									number = number.replace(".", "");
									number = number.replace(",", ".");
								}else if (number.contains(".")&&!number.contains(",")) {
									number = number.replace(".", "");
								}else if (!number.contains(".")&&number.contains(",")) {
									number = number.replace(",", ".");
								}
								UpdateRow(Long.parseLong(entryPersonalDB.getPersonKeyPersonRowId(adapter2.getItem(position).toString())[position]),position,PersonalrowItems2.get(position).getDate(),PersonalrowItems2.get(position).getPerson(),PersonalrowItems2.get(position).getProduct(),number);
								RefreshPersonalList();
								dialog.dismiss();
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
				
				lvDetails.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							final int position, long id) {
						if (lvDetails.getOnItemLongClickListener() != null) {
							Toast.makeText(getActivity(), "'"+PersonalrowItems2.get(position).getDate().replace("\n", "")+" "+PersonalrowItems2.get(position).getPerson()+" "+PersonalrowItems2.get(position).getAmount()+"' Veriyi silmek yada düzeltmek için uzun týklayýnýz...", Toast.LENGTH_LONG).show();	
						}						
					}
				});
				
			}
		});			
		
		bVeriEkle = (Button) rootView.findViewById(R.id.bVeriEkle);
		bVeriEkle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Veri Ekle			
				final Dialog dialog;
				dialog = new Dialog(rootView.getContext(),android.R.style.Theme_Translucent);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setCancelable(true);
				dialog.setContentView(R.layout.personal_add);
				dialog.show();				

			    ArrayAdapter<String> mAdapter;
			    spinner = (Spinner) dialog.findViewById(R.id.spinner1);
			    mAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, duplicateddataPerson);
				spinner.setAdapter(mAdapter);
				
				
				final LinearLayout llPersonLayout = (LinearLayout) dialog.findViewById(R.id.llPersonLayout);
				final LinearLayout llPersonSpinnerLayout = (LinearLayout) dialog.findViewById(R.id.llPersonSpinnerLayout);
				if (entryPersonalDB.getPerson().length!=0) {
					llPersonLayout.setVisibility(View.GONE);
					llPersonSpinnerLayout.setVisibility(View.VISIBLE);
				}else{
					llPersonLayout.setVisibility(View.VISIBLE);
					llPersonSpinnerLayout.setVisibility(View.GONE);
				}
			
				final EditText etPerson = (EditText) dialog.findViewById(R.id.etPerson);
				final EditText etProduct = (EditText) dialog.findViewById(R.id.etProduct);
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
								result = entryPersonalDB.createEntry(currentDateandTime, etPerson.getText().toString(), etProduct.getText().toString(), etAmount.getText().toString());
							}else{
								result =  entryPersonalDB.createEntry(currentDateandTime, spinner.getSelectedItem().toString(), etProduct.getText().toString(), etAmount.getText().toString());
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
		
		bDetaylar = (Button) rootView.findViewById(R.id.bDetails);
		bDetaylar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Detaylar
				RefreshPersonalList3();
				if (!adapter2.isEmpty()) {
					dialog.show();	
				}else{
					Toast.makeText(getActivity(), "Herhangi bir veri giriþi yapýlmadý.", Toast.LENGTH_SHORT).show();
				}
				lvDetails.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, final int position, long id) {
						// TODO Detaylar ListView
						AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
						builder.setMessage("'"+PersonalrowItems2.get(position).getDate().replace("\n", "")+" - "+PersonalrowItems2.get(position).getPerson()+" "+PersonalrowItems2.get(position).getProduct()+" "+PersonalrowItems2.get(position).getAmount()+"' Veriyi silmek istediðinize emin misiniz? ");
						builder.setCancelable(true);
						
						builder.setPositiveButton("Sil",
			                    new DialogInterface.OnClickListener() {
			                public void onClick(DialogInterface dialog2, int id) {
			                	//entryPersonalDB.deleteSpesific(adapter2.getItem(position).toString());		                	
			                	AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
					            //builder1.setMessage("'" + entryPersonalDB.getAllDataWhereDate(entryPersonalDB.getPersonKeyDate(adapter2.getItem(position).toString())[position]) + "'adlý veriyi silmek istediðinize emin misiniz?");
			                	builder.setMessage("'"+PersonalrowItems2.get(position).getDate().replace("\n", "")+" "+PersonalrowItems2.get(position).getPerson()+" "+PersonalrowItems2.get(position).getProduct()+" "+PersonalrowItems2.get(position).getAmount()+"' Veriyi silmek istediðinize emin misiniz?");
			                	builder.setCancelable(true);
			                	builder.setPositiveButton("Evet",
					                    new DialogInterface.OnClickListener() {
					                public void onClick(DialogInterface dialog23, int id) {
					                	
					                	String[] parts = PersonalrowItems2.get(position).getAmount().split(" ");
										String number = parts[0];
										if (number.contains(".")&&number.contains(",")) {
											number = number.replace(".", "");
											number = number.replace(",", ".");
										}else if (number.contains(".")&&!number.contains(",")) {
											number = number.replace(".", "");
										}else if (!number.contains(".")&&number.contains(",")) {
											number = number.replace(",", ".");
										}
					                	boolean returned = entryPersonalDB.deleteToDo(Long.parseLong(entryPersonalDB.getPersonKeyAllRowId(PersonalrowItems2.get(position).getDate(),PersonalrowItems2.get(position).getPerson(),PersonalrowItems2.get(position).getProduct(),number)[0]));
					                	
					                	//Toast.makeText(getActivity(), String.valueOf(entryPersonalDB.getDateKeyRowId(PersonalrowItems2.get(position).getDate())[0]), Toast.LENGTH_SHORT).show();
					                	if (returned) {
						                	Toast.makeText(getActivity(), "Silindi", Toast.LENGTH_SHORT).show();
						                	if (loaded) {
												soundPool.play(negativeSound,  0.5f,0.5f, 1, 0, 1f);
											}
						                	RefreshPersonalList();
						                	dialog23.dismiss();
						                	
						    				if (adapter.isEmpty()) {
												dialog.dismiss();
											}else{
												RefreshPersonalList3();
											}
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
						
						builder.setNeutralButton("Düzelt", new DialogInterface.OnClickListener() {				
							@Override
							public void onClick(DialogInterface dialog15, int which) {
								// TODO Auto-generated method stub
								dialog15.dismiss();
								String[] parts = PersonalrowItems2.get(position).getAmount().split(" ");
								String number = parts[0];
								if (number.contains(".")&&number.contains(",")) {
									number = number.replace(".", "");
									number = number.replace(",", ".");
								}else if (number.contains(".")&&!number.contains(",")) {
									number = number.replace(".", "");
								}else if (!number.contains(".")&&number.contains(",")) {
									number = number.replace(",", ".");
								}
								UpdateRow(Long.parseLong(entryPersonalDB.getPersonKeyAllRowId(PersonalrowItems2.get(position).getDate(),PersonalrowItems2.get(position).getPerson(),PersonalrowItems2.get(position).getProduct(),number)[0]),position,PersonalrowItems2.get(position).getDate(),PersonalrowItems2.get(position).getPerson(),PersonalrowItems2.get(position).getProduct(),number);
								RefreshPersonalList();
								dialog.dismiss();
							}
						});
						
						builder.setNegativeButton("Vazgeç",  new DialogInterface.OnClickListener() {
			                public void onClick(DialogInterface dialog2, int id) {
			                    dialog2.dismiss();
			                }
			            });
						

			            AlertDialog alertDialog = builder.create();
			            alertDialog.show();
						return true;
					}
				});
				
				lvDetails.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							final int position, final long row_id) {
						if (lvDetails.getOnItemLongClickListener() != null) {
							Toast.makeText(getActivity(), "'"+PersonalrowItems2.get(position).getDate().replace("\n", "")+" - "+PersonalrowItems2.get(position).getPerson()+" "+PersonalrowItems2.get(position).getAmount()+"' Veriyi silmek yada düzeltmek için uzun týklayýnýz...", Toast.LENGTH_LONG).show();	
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
				if (!adapter.isEmpty()) {
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
					builder.setMessage("Tüm Personel verilerini silmek istediðinize emin misiniz?");
					builder.setCancelable(true);
					builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
		                public void onClick(DialogInterface dialog2, int id) {
		                	entryPersonalDB.deleteAllData();
		                	Toast.makeText(getActivity(), "Tüm Personel Verileri Silindi", Toast.LENGTH_SHORT).show();
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
	
	private void ListViewContentConfigDetails() {
		// TODO Auto-generated method stub
		ListView lvContentDetails;
		List<RowItemFour> ContentrowItemsDetails;
		CustomListViewAdapterFour contentAdapterDetails;
		lvContentDetails = (ListView) dialog.findViewById(R.id.lvSelectedPersonalContentlist);
		ContentrowItemsDetails = new ArrayList<RowItemFour>();
		contentAdapterDetails = new CustomListViewAdapterFour(getActivity(),R.layout.content_list_item2, ContentrowItemsDetails);
		lvContentDetails.setAdapter(contentAdapterDetails);
		contentAdapterDetails.clear();
		RowItemFour item2 = new RowItemFour("Tarih","Personel","Ürün", "Miktar\n(TL)" );		
		ContentrowItemsDetails.add(item2);
		contentAdapterDetails.notifyDataSetChanged();
	}

	private void ListViewContentConfig() {
		// TODO ListViewContentConfig
		ListView lvContent;
		List<RowItemTwo> ContentrowItems;
		CustomListViewAdapterTwo contentAdapter;
		lvContent = (ListView) rootView.findViewById(R.id.lvContent);
		ContentrowItems = new ArrayList<RowItemTwo>();
		contentAdapter = new CustomListViewAdapterTwo(getActivity(),R.layout.content_list_item, ContentrowItems);
		lvContent.setAdapter(contentAdapter);
		contentAdapter.clear();
		RowItemTwo item2 = new RowItemTwo("Personel", "Miktar" );		
		ContentrowItems.add(item2);
		contentAdapter.notifyDataSetChanged();
	}

	public void RefreshPersonalList(){
		adapter.clear();
		String[] dataPerson = entryPersonalDB.getPerson();
		String[] dataAmount = entryPersonalDB.getAmount();
		
		List<String> list = Arrays.asList(dataPerson);
		Set<String> set = new HashSet<String>(list);
		duplicateddataPerson= new String[set.size()];
		set.toArray(duplicateddataPerson);
		
		DecimalFormat format = new DecimalFormat();
		
		for (int i = 0 ; i < duplicateddataPerson.length ; i++) {
		 //Log.d("DATA",  String.valueOf(entryPersonalDB.getPersonWhere(duplicateddataPerson[i]).length));
			double sumValue = 0;
			for (int j = 0 ; j < entryPersonalDB.getPersonKeyAmount(duplicateddataPerson[i]).length ; j++) {
				if (entryPersonalDB.getPersonKeyAmount(duplicateddataPerson[i])[j] != null) {
					Log.d("DATA i = " +String.valueOf(i) + " j = " + String.valueOf(j) , String.valueOf(entryPersonalDB.getPersonKeyAmount(duplicateddataPerson[i])[j]));
					sumValue = sumValue + Double.valueOf(entryPersonalDB.getPersonKeyAmount(duplicateddataPerson[i])[j]);
				}	 
			}
			RowItem item = new RowItem(duplicateddataPerson[i], String.valueOf(format.format(sumValue))+" TL" );		
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
	
	public void RefreshPersonalList2(int position){
		adapter2.clear();
		String[] dataPersonKeyDate = entryPersonalDB.getPersonKeyDate(adapter.getItem(position).toString());
		String[] dataPersonKeyPerson = entryPersonalDB.getPersonKeyPerson(adapter.getItem(position).toString());
		String[] dataPersonKeyProduct = entryPersonalDB.getPersonKeyProduct(adapter.getItem(position).toString());
		String[] dataPersonKeyAmount = entryPersonalDB.getPersonKeyAmount(adapter.getItem(position).toString());
		DecimalFormat format = new DecimalFormat();
		for (int i = 0 ; i < dataPersonKeyPerson.length ; i++) {
		 //Log.d("DATA",  String.valueOf(entryPersonalDB.getPersonWhere(duplicateddataPerson[i]).length));
			if (dataPersonKeyPerson[i] != null) {
				RowItem2 item = new RowItem2(dataPersonKeyDate[i], dataPersonKeyPerson[i],dataPersonKeyProduct[i],format.format(Double.valueOf(dataPersonKeyAmount[i]))+" TL");		
				PersonalrowItems2.add(item);
			}
		 }
		adapter2.notifyDataSetChanged();
	}
	
	public void RefreshPersonalList3(){
		adapter2.clear();
		String[] dataDate = entryPersonalDB.getDate();
		String[] dataPerson = entryPersonalDB.getPerson();
		String[] dataProduct = entryPersonalDB.getProduct();
		String[] dataAmount = entryPersonalDB.getAmount();
		DecimalFormat format = new DecimalFormat();
		for (int i = 0 ; i < dataDate.length ; i++) {
		 //Log.d("DATA",  String.valueOf(entryPersonalDB.getPersonWhere(duplicateddataPerson[i]).length));
			if (dataDate[i] != null) {
				RowItem2 item = new RowItem2(dataDate[i], dataPerson[i],dataProduct[i],format.format(Double.valueOf(dataAmount[i]))+" TL");		
				PersonalrowItems2.add(item);
			}
		 }
		adapter2.notifyDataSetChanged();
	}
	
	@Override
	public void onResume() {
		// TODO OnResume
		super.onResume();
		entryPersonalDB = new PersonalDB(getActivity());
		entryPersonalDB.open();
		RefreshPersonalList();
	};
	
	@Override
	public void onPause() {
		// TODO OnPause
		super.onPause();
		entryPersonalDB.close();
	}

	@Override
	public void onDestroy() {
		// TODO OnDestroy
		super.onDestroy();
		entryPersonalDB.close();
	}
	
	private void UpdateRow(final long RowID,int position,String date,String person,String product,String amount) {
		// TODO Auto-generated method stub
		final Dialog dialog;
		dialog = new Dialog(rootView.getContext(),android.R.style.Theme_Translucent);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(true);
		dialog.setContentView(R.layout.personal_fix);
		dialog.show();					
		
		final TextView tvFix = (TextView) dialog.findViewById(R.id.tvFixit);
		tvFix.setText("'"+date.replace("\n","")+" "+person+" "+product+" "+amount+"' Veriler düzeltiliyor...");
		final EditText etPerson = (EditText) dialog.findViewById(R.id.etPerson);
		etPerson.setText(person);
		final EditText etProduct = (EditText) dialog.findViewById(R.id.etProduct);
		etProduct.setText(product);
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
					result = entryPersonalDB.updateEntry(currentDateandTime, etPerson.getText().toString(), etProduct.getText().toString(), etAmount.getText().toString(),String.valueOf(RowID));
									
					RefreshPersonalList();
					RefreshPersonalList3();
					
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