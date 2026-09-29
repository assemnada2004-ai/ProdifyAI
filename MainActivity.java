package com.prodifyai.app;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.graphics.drawable.GradientDrawable;

public class MainActivity extends Activity {
    LinearLayout root, content;
    ImageView preview;
    TextView status;
    Button generate;
    Uri selectedImage;
    int purple = Color.rgb(124,58,237), pink=Color.rgb(217,70,239), navy=Color.rgb(8,13,42), white=Color.WHITE, muted=Color.rgb(169,177,199);

    @Override public void onCreate(Bundle b){ super.onCreate(b); showHome(); }

    TextView text(String s, float size, int color, boolean bold){
        TextView t=new TextView(this); t.setText(s); t.setTextSize(size); t.setTextColor(color); t.setGravity(Gravity.CENTER_VERTICAL);
        if(bold)t.setTypeface(Typeface.DEFAULT,Typeface.BOLD); t.setPadding(0,4,0,4); return t;
    }
    GradientDrawable bg(int color,float r){ GradientDrawable g=new GradientDrawable(); g.setColor(color); g.setCornerRadius(r); return g; }
    Button btn(String label){ Button b=new Button(this); b.setText(label); b.setTextColor(white); b.setTextSize(15); b.setAllCaps(false); b.setTypeface(Typeface.DEFAULT,Typeface.BOLD); b.setBackground(bg(purple,50)); b.setPadding(20,4,20,4); return b; }
    LinearLayout row(){ LinearLayout l=new LinearLayout(this); l.setOrientation(LinearLayout.HORIZONTAL); l.setGravity(Gravity.CENTER_VERTICAL); return l; }
    void base(){
        root=new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL); root.setBackgroundColor(navy);
        ScrollView scroll=new ScrollView(this); scroll.setFillViewport(true); content=new LinearLayout(this); content.setOrientation(LinearLayout.VERTICAL); content.setPadding(22,18,22,28); scroll.addView(content); root.addView(scroll,new LinearLayout.LayoutParams(-1,0,1)); setContentView(root);
    }
    void showHome(){
        base();
        LinearLayout top=row(); TextView logo=text("P",30,white,true); logo.setGravity(Gravity.CENTER); logo.setBackground(bg(pink,22)); top.addView(logo,new LinearLayout.LayoutParams(58,58));
        LinearLayout names=new LinearLayout(this); names.setOrientation(LinearLayout.VERTICAL); names.setPadding(14,0,0,0); TextView n=text("Prodify AI",24,white,true); TextView sub=text("Your Product Deserves Better",12,muted,false); names.addView(n);names.addView(sub);top.addView(names,new LinearLayout.LayoutParams(0,70,1)); content.addView(top);
        TextView hero=text("حوّل صورة منتجك\nإلى إعلان احترافي بالذكاء الاصطناعي",28,white,true); hero.setGravity(Gravity.CENTER); hero.setPadding(0,40,0,18); content.addView(hero,new LinearLayout.LayoutParams(-1,150));
        TextView desc=text("ارفع صورة المنتج، اختار نوع الإعلان، وخلّي Prodify AI يجهز لك صورة تسويقية جاهزة للنشر.",15,muted,false); desc.setGravity(Gravity.CENTER); content.addView(desc,new LinearLayout.LayoutParams(-1,80));
        generate=btn("✨  اختر صورة المنتج"); generate.setOnClickListener(v->pickImage()); content.addView(generate,new LinearLayout.LayoutParams(-1,58));
        TextView or=text("أو جرّب أحد القوالب",14,muted,true); or.setGravity(Gravity.CENTER); or.setPadding(0,24,0,10); content.addView(or);
        LinearLayout chips=row(); String[] types={"🛍️ متجر","👟 أحذية","🌸 عطور","⌚ إكسسوارات"}; for(String x:types){ Button c=btn(x); c.setTextSize(12); c.setBackground(bg(Color.rgb(17,23,53),40)); LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(0,50,1);p.setMargins(4,0,4,0);chips.addView(c,p);} content.addView(chips);
        TextView f=text("\nمزايا النسخة الأولى\n\n• رفع صور المنتجات من الهاتف\n• قوالب إعلانية متعددة\n• معاينة قبل الحفظ\n• تصميم عربي / English\n• بنية جاهزة لربط مولّد الصور بالـAI",15,white,false); content.addView(f);
    }
    void pickImage(){ Intent i=new Intent(Intent.ACTION_OPEN_DOCUMENT); i.setType("image/*"); i.addCategory(Intent.CATEGORY_OPENABLE); startActivityForResult(i,77); }
    @Override protected void onActivityResult(int r,int c,Intent d){ super.onActivityResult(r,c,d); if(r==77 && c==RESULT_OK && d!=null){ selectedImage=d.getData(); showEditor(); } }
    void showEditor(){
        base(); content.addView(text("إنشاء إعلان جديد",26,white,true)); content.addView(text("الصورة الأصلية",14,muted,false));
        preview=new ImageView(this); preview.setScaleType(ImageView.ScaleType.CENTER_CROP); preview.setImageURI(selectedImage); preview.setBackground(bg(Color.rgb(17,23,53),24)); LinearLayout.LayoutParams ip=new LinearLayout.LayoutParams(-1,320);ip.setMargins(0,12,0,18);content.addView(preview,ip);
        content.addView(text("نوع الإعلان",15,white,true));
        Spinner spinner=new Spinner(this); String[] opts={"إعلان منتج احترافي","بوست متجر إلكتروني","Story / Reels","خصم وعروض","خلفية فاخرة"}; ArrayAdapter<String>a=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,opts);spinner.setAdapter(a); content.addView(spinner,new LinearLayout.LayoutParams(-1,55));
        content.addView(text("وصف إضافي (اختياري)",15,white,true)); EditText prompt=new EditText(this);prompt.setHint("مثال: خلفية سوداء فاخرة مع إضاءة ذهبية");prompt.setHintTextColor(muted);prompt.setTextColor(white);prompt.setBackground(bg(Color.rgb(17,23,53),20));prompt.setPadding(18,0,18,0);content.addView(prompt,new LinearLayout.LayoutParams(-1,60));
        Button make=btn("✨  إنشاء الصورة الإعلانية"); make.setOnClickListener(v->showResult(prompt.getText().toString())); LinearLayout.LayoutParams mp=new LinearLayout.LayoutParams(-1,60);mp.setMargins(0,20,0,0);content.addView(make,mp);
        status=text("وضع التجربة: سيتم ربط مولّد الصور الحقيقي في خطوة الـAI التالية.",12,muted,false);status.setGravity(Gravity.CENTER);content.addView(status,new LinearLayout.LayoutParams(-1,60));
    }
    void showResult(String prompt){
        base(); content.addView(text("نتيجة Prodify AI",26,white,true)); content.addView(text("معاينة الإعلان",14,muted,false));
        LinearLayout card=new LinearLayout(this);card.setOrientation(LinearLayout.VERTICAL);card.setPadding(16,16,16,16);card.setBackground(bg(Color.rgb(17,23,53),28));
        ImageView out=new ImageView(this);out.setImageURI(selectedImage);out.setScaleType(ImageView.ScaleType.CENTER_CROP);card.addView(out,new LinearLayout.LayoutParams(-1,420));
        TextView badge=text("PRODIFY AI  •  AD PREVIEW",14,white,true);badge.setGravity(Gravity.CENTER);badge.setPadding(0,16,0,8);card.addView(badge);
        TextView p=text(prompt.length()>0?prompt:"Professional product advertising",13,muted,false);p.setGravity(Gravity.CENTER);card.addView(p,new LinearLayout.LayoutParams(-1,60));content.addView(card);
        Button save=btn("⬇  حفظ الصورة");LinearLayout.LayoutParams sp=new LinearLayout.LayoutParams(-1,58);sp.setMargins(0,18,0,8);content.addView(save,sp);save.setOnClickListener(v->Toast.makeText(this,"الحفظ الفعلي سيُفعّل مع محرك التوليد النهائي.",Toast.LENGTH_LONG).show());
        Button back=btn("＋  إنشاء إعلان آخر");back.setBackground(bg(Color.rgb(17,23,53),50));content.addView(back,new LinearLayout.LayoutParams(-1,58));back.setOnClickListener(v->showHome());
    }
}
