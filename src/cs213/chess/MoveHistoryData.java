package cs213.chess;

import java.io.Serializable;
import java.util.ArrayList;

public class MoveHistoryData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3871300896235596188L;
	ArrayList<Integer[]> MoveLog;
	
	public MoveHistoryData()
	{
		MoveLog = new ArrayList<Integer[]>();
	}
}
