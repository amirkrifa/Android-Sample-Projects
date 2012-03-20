package com.MoBEEVents;

import android.app.AlertDialog;
import android.content.Context;



public class Utilities {
	
	public static void ShowDialog(Context ctx, String msg)
	{
		AlertDialog.Builder adb = new AlertDialog.Builder(ctx);
		//on attribut un titre à notre boite de dialogue
		adb.setTitle("Infos");
		//on insère un message à notre boite de dialogue, et ici on affiche le titre de l'item cliqué
		adb.setMessage(msg);
		//on indique que l'on veut le bouton ok à notre boite de dialogue
		adb.setPositiveButton("Ok", null);
		//on affiche la boite de dialogue
		adb.show();
	}

}
