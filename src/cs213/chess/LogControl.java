package cs213.chess;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

import android.content.Context;

public class LogControl
{
	MoveHistoryData log;
	Context context;
	String[] fileList;
	
	public LogControl(Context context)
	{
		log = new MoveHistoryData();
		this.context = context;
	}
	
	public void SetLog(ArrayList<Integer[]> log)
	{
		this.log.MoveLog = log;
	}
	
	public void saveGame(String SaveFileName)
	{
		ObjectOutputStream output;
		try {
			output = new ObjectOutputStream(context.openFileOutput(SaveFileName, Context.MODE_PRIVATE));
			output.writeObject(log);
			output.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String[] getSavedFiles()
	{
		this.fileList = context.fileList();
		return this.fileList;
	}
	
	public void loadGame(int itemNumber)
	{
		ObjectInputStream input;
		
		try {
			String LoadFileName = this.fileList[itemNumber];
			input = new ObjectInputStream(context.openFileInput(LoadFileName));
			
			try {
				
				this.log = (MoveHistoryData) input.readObject();
			
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void removeGame(int itemNumber)
	{
		String loadFileName = this.fileList[itemNumber];
		context.deleteFile(loadFileName);
	}
}
