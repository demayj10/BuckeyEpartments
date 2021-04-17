package android.ohiostate.buckeyepartments;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="buckeyepartments.db";
    private static final String APARTMENTS_TABLE="apartments_table";
    private static final int DATABASE_Version = 1;

    private static  final  String apartment_id="apartment_id";
    private static  final  String fbKey="firebaseKey";
    private static  final  String streetAddress="streetAddress";
    private static  final  String rent="rent";
    private static  final  String bed="bed";
    private static  final  String bath="bath";
    private static  final  String imageURL="imageURL";
    private static  final  String city="city";
    private static  final  String zip="zip";

    public String CREATE_APARTMENTS_TABLE = "CREATE TABLE " + APARTMENTS_TABLE + "("
            + apartment_id + "  INTEGER PRIMARY KEY AUTOINCREMENT, "
            + fbKey+ " TEXT, "       //index 1
            + streetAddress+ " TEXT, " //index 2
            + rent+ " TEXT, "   //index 3
            + bed+ " TEXT, "    //index 4
            + bath+ " TEXT, "    //index 5
            + imageURL+ " TEXT, "  //index 6
            + city+ " TEXT, "     //index 7
            + zip+ " TEXT )";     //index 8

    private static final String DROP_APARTMENTS_TABLE ="DROP TABLE IF EXISTS "+APARTMENTS_TABLE;
    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_Version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_APARTMENTS_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(CREATE_APARTMENTS_TABLE);
        onCreate(db);
    }
    public boolean InsertApartment(String Key,
                                   String StreetAddress,
                                   String Rent,
                                   String Bed,
                                   String Bath,
                                   String ImageURL,
                                   String City,
                                   String Zip){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv= new ContentValues();
        cv.put(fbKey,Key);
        cv.put(streetAddress,StreetAddress);
        cv.put(rent,Rent);
        cv.put(bed,Bed);
        cv.put(bath,Bath);
        cv.put(imageURL,ImageURL);
        cv.put(city,City);
        cv.put(zip,Zip);
        long result=db.insert(APARTMENTS_TABLE,null,cv);
        db.close();
        return result != -1;
    }
    public Cursor getAllApartments(){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursorLocation=db.rawQuery(" select * from "+APARTMENTS_TABLE,null );
        return cursorLocation;
    }
    public  Cursor getSingleApartment(String id){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery(" select * from "+ APARTMENTS_TABLE +" where " + fbKey+"='"+ id +"'",null );
        return cursor;

    }
    public void deleteApartment(int id){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(APARTMENTS_TABLE, apartment_id+"="+id, null);
        db.close();
    }

}