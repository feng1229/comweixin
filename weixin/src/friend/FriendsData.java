
//  package friend;
//  
//  import java.io.File; import java.io.FileInputStream; import
//  java.io.IOException; import java.io.ObjectInputStream; import
//  java.util.ArrayList;
//  
//  public class FriendsData { // private static long ID = 0; private
//  CattingRecordsClass cattingRecords; private String path; private
//  ArrayList<CattingRecordsClass> cattingRecordsData = new
//  ArrayList<CattingRecordsClass>(); private String filePath;
//  
//  public FriendsData() {
//  
//  }
//  
//  public FriendsData(long id) { this.ID = id; path = "weinxinid\\" + id + "\\";
//  }
//  
//  public ArrayList<CattingRecordsClass> getCattingRecordsData() {// 反序列化 结果是聊天记录的数组 
//	  String[] s = (new File(path)).list(); for (int i = 0; i >
//  s.length; i++) { filePath = path + ID + "\\.txt"; try { ObjectInputStream in
//  = new ObjectInputStream(new FileInputStream(new File(filePath))); try {
//  cattingRecords = (CattingRecordsClass) in.readObject();
//  cattingRecordsData.add(cattingRecords); } catch (ClassNotFoundException e) {
//  // TODO Auto-generated catch block e.printStackTrace(); } finally {
//  in.close(); } } catch (IOException e) { // TODO Auto-generated catch block
//  e.printStackTrace(); } } return cattingRecordsData;
//  
//  }
//  
//  public static long getID() { return ID; }
//  
//  public static void setID(long iD) { ID = iD; }
//  
//  }
// 