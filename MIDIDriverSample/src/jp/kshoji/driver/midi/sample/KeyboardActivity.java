package jp.kshoji.driver.midi.sample;

import android.app.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import jp.kshoji.javax.sound.midi.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.widget.*;
import android.support.v7.app.*;

public class KeyboardActivity extends Activity
{
	
	float rotation = 0.0f;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.piano_view_layout);
		
		OnTouchListener onToneButtonTouchListenerTop = new OnTouchListener() {





			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				String tagKey = v.getTag().toString();


				switch (event.getAction())
				{
					case MotionEvent.ACTION_DOWN:


						v.setBackgroundResource(R.drawable.rect_blue_key);


						break;
					case MotionEvent.ACTION_UP:

						if(tagKey.equalsIgnoreCase("101") || tagKey.equalsIgnoreCase("103") || 
						   tagKey.equalsIgnoreCase("106") || tagKey.equalsIgnoreCase("108") || 
						   tagKey.equalsIgnoreCase("110")){


							//testToast("hello");
							v.setBackgroundResource(R.drawable.rect_black_key);




							break;

						}else if (tagKey.equalsIgnoreCase("100") || tagKey.equalsIgnoreCase("102") || 
								  tagKey.equalsIgnoreCase("104") || tagKey.equalsIgnoreCase("105") || 
								  tagKey.equalsIgnoreCase("107") || tagKey.equalsIgnoreCase("109") || 
								  tagKey.equalsIgnoreCase("111") || tagKey.equalsIgnoreCase("112")){


							v.setBackgroundResource(R.drawable.rect_white_key);
							break;

						}
						


						break;
					default:
						// do nothing.
						break;
				}
				return false;
			}


		};
		
		
		OnTouchListener onToneButtonTouchListenerBottom = new OnTouchListener() {





			@Override
			public boolean onTouch(View v, MotionEvent event)
			{


				String tagKey = v.getTag().toString();
				//int note = 60 + Integer.parseInt((String) v.getTag())+intTranspose;



				switch (event.getAction())
				{
					case MotionEvent.ACTION_DOWN:


				

						v.setBackgroundResource(R.drawable.rect_blue_key);



						break;
					case MotionEvent.ACTION_UP:
						
					
						if(tagKey.equalsIgnoreCase("1") || tagKey.equalsIgnoreCase("3") || 
						tagKey.equalsIgnoreCase("6") || tagKey.equalsIgnoreCase("8") || 
						tagKey.equalsIgnoreCase("10")){
							
							
							//testToast("hello");
							v.setBackgroundResource(R.drawable.rect_black_key);
							
							
							
							
							break;
							
						}else if (tagKey.equalsIgnoreCase("0") || tagKey.equalsIgnoreCase("2") || 
						tagKey.equalsIgnoreCase("4") || tagKey.equalsIgnoreCase("5") || 
						tagKey.equalsIgnoreCase("7") || tagKey.equalsIgnoreCase("9") || 
						tagKey.equalsIgnoreCase("11") || tagKey.equalsIgnoreCase("12")){
							
							
							v.setBackgroundResource(R.drawable.rect_white_key);
							break;
							
						}
						
						

						break;
					default:
						// do nothing.
						break;
				}
				return false;
			}


		};
		//oberes Keyboard
		
		findViewById(R.id.buttonC).setOnTouchListener(onToneButtonTouchListenerTop);
		findViewById(R.id.buttonCis).setOnTouchListener(onToneButtonTouchListenerTop);
		findViewById(R.id.buttonD).setOnTouchListener(onToneButtonTouchListenerTop);
		findViewById(R.id.buttonDis).setOnTouchListener(onToneButtonTouchListenerTop);
		findViewById(R.id.buttonE).setOnTouchListener(onToneButtonTouchListenerTop);
		findViewById(R.id.buttonF).setOnTouchListener(onToneButtonTouchListenerTop);
		findViewById(R.id.buttonFis).setOnTouchListener(onToneButtonTouchListenerTop);
		findViewById(R.id.buttonG).setOnTouchListener(onToneButtonTouchListenerTop);
		findViewById(R.id.buttonGis).setOnTouchListener(onToneButtonTouchListenerTop);
		findViewById(R.id.buttonA).setOnTouchListener(onToneButtonTouchListenerTop);
		findViewById(R.id.buttonAis).setOnTouchListener(onToneButtonTouchListenerTop);
		findViewById(R.id.buttonB).setOnTouchListener(onToneButtonTouchListenerTop);
		findViewById(R.id.buttonC2).setOnTouchListener(onToneButtonTouchListenerTop);

		//int whiteKeyColor = 0xFFFFFFFF;
		//int blackKeyColor = 0xFF808080;
		findViewById(R.id.buttonC).setBackgroundResource(R.drawable.rect_white_key); //getBackground().setColorFilter(whiteKeyColor, Mode.MULTIPLY);
		findViewById(R.id.buttonCis).setBackgroundResource(R.drawable.rect_black_key);// getBackground().setColorFilter(blackKeyColor, Mode.MULTIPLY);
		findViewById(R.id.buttonD).setBackgroundResource(R.drawable.rect_white_key); //getBackground().setColorFilter(whiteKeyColor, Mode.MULTIPLY);
		findViewById(R.id.buttonDis).setBackgroundResource(R.drawable.rect_black_key);//getBackground().setColorFilter(blackKeyColor, Mode.MULTIPLY);
		findViewById(R.id.buttonE).setBackgroundResource(R.drawable.rect_white_key); //getBackground().setColorFilter(whiteKeyColor, Mode.MULTIPLY);
		findViewById(R.id.buttonF).setBackgroundResource(R.drawable.rect_white_key); //getBackground().setColorFilter(whiteKeyColor, Mode.MULTIPLY);
		findViewById(R.id.buttonFis).setBackgroundResource(R.drawable.rect_black_key);//getBackground().setColorFilter(blackKeyColor, Mode.MULTIPLY);
		findViewById(R.id.buttonG).setBackgroundResource(R.drawable.rect_white_key); //getBackground().setColorFilter(whiteKeyColor, Mode.MULTIPLY);
		findViewById(R.id.buttonGis).setBackgroundResource(R.drawable.rect_black_key);//getBackground().setColorFilter(blackKeyColor, Mode.MULTIPLY);
		findViewById(R.id.buttonA).setBackgroundResource(R.drawable.rect_white_key); //getBackground().setColorFilter(whiteKeyColor, Mode.MULTIPLY);
		findViewById(R.id.buttonAis).setBackgroundResource(R.drawable.rect_black_key);//getBackground().setColorFilter(blackKeyColor, Mode.MULTIPLY);
		findViewById(R.id.buttonB).setBackgroundResource(R.drawable.rect_white_key); //getBackground().setColorFilter(whiteKeyColor, Mode.MULTIPLY);
		findViewById(R.id.buttonC2).setBackgroundResource(R.drawable.rect_white_key); //getBackground().setColorFilter(whiteKeyColor, Mode.MULTIPLY);
		
		//unteres Kwyboard
		
		
		findViewById(R.id.buttonCu).setOnTouchListener(onToneButtonTouchListenerBottom);
		findViewById(R.id.buttonCisu).setOnTouchListener(onToneButtonTouchListenerBottom);
		findViewById(R.id.buttonDu).setOnTouchListener(onToneButtonTouchListenerBottom);
		findViewById(R.id.buttonDisu).setOnTouchListener(onToneButtonTouchListenerBottom);
		findViewById(R.id.buttonEu).setOnTouchListener(onToneButtonTouchListenerBottom);
		findViewById(R.id.buttonFu).setOnTouchListener(onToneButtonTouchListenerBottom);
		findViewById(R.id.buttonFisu).setOnTouchListener(onToneButtonTouchListenerBottom);
		findViewById(R.id.buttonGu).setOnTouchListener(onToneButtonTouchListenerBottom);
		findViewById(R.id.buttonGisu).setOnTouchListener(onToneButtonTouchListenerBottom);
		findViewById(R.id.buttonAu).setOnTouchListener(onToneButtonTouchListenerBottom);
		findViewById(R.id.buttonAisu).setOnTouchListener(onToneButtonTouchListenerBottom);
		findViewById(R.id.buttonBu).setOnTouchListener(onToneButtonTouchListenerBottom);
		findViewById(R.id.buttonC2u).setOnTouchListener(onToneButtonTouchListenerBottom);

		//int whiteKeyColor = 0xFFFFFFFF;
		//int blackKeyColor = 0xFF808080;
		findViewById(R.id.buttonCu).setBackgroundResource(R.drawable.rect_white_key); //getBackground().setColorFilter(whiteKeyColor, Mode.MULTIPLY);
		findViewById(R.id.buttonCisu).setBackgroundResource(R.drawable.rect_black_key);// getBackground().setColorFilter(blackKeyColor, Mode.MULTIPLY);
		findViewById(R.id.buttonDu).setBackgroundResource(R.drawable.rect_white_key); //getBackground().setColorFilter(whiteKeyColor, Mode.MULTIPLY);
		findViewById(R.id.buttonDisu).setBackgroundResource(R.drawable.rect_black_key);//getBackground().setColorFilter(blackKeyColor, Mode.MULTIPLY);
		findViewById(R.id.buttonEu).setBackgroundResource(R.drawable.rect_white_key); //getBackground().setColorFilter(whiteKeyColor, Mode.MULTIPLY);
		findViewById(R.id.buttonFu).setBackgroundResource(R.drawable.rect_white_key); //getBackground().setColorFilter(whiteKeyColor, Mode.MULTIPLY);
		findViewById(R.id.buttonFisu).setBackgroundResource(R.drawable.rect_black_key);//getBackground().setColorFilter(blackKeyColor, Mode.MULTIPLY);
		findViewById(R.id.buttonGu).setBackgroundResource(R.drawable.rect_white_key); //getBackground().setColorFilter(whiteKeyColor, Mode.MULTIPLY);
		findViewById(R.id.buttonGisu).setBackgroundResource(R.drawable.rect_black_key);//getBackground().setColorFilter(blackKeyColor, Mode.MULTIPLY);
		findViewById(R.id.buttonAu).setBackgroundResource(R.drawable.rect_white_key); //getBackground().setColorFilter(whiteKeyColor, Mode.MULTIPLY);
		findViewById(R.id.buttonAisu).setBackgroundResource(R.drawable.rect_black_key);//getBackground().setColorFilter(blackKeyColor, Mode.MULTIPLY);
		findViewById(R.id.buttonBu).setBackgroundResource(R.drawable.rect_white_key); //getBackground().setColorFilter(whiteKeyColor, Mode.MULTIPLY);
		findViewById(R.id.buttonC2u).setBackgroundResource(R.drawable.rect_white_key); //getBackground().setColorFilter(whiteKeyColor, Mode.MULTIPLY);
		
		
		findViewById(R.id.btnRotatePiano).setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					
					rotation=rotation+180.0f;
					findViewById(R.id.layButtTastOben).setRotation(rotation);
					
				}

			
			
			
			
		});
		
		
	}
	
	public void testToast(String s)
	{

		Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();


	}
	
}
