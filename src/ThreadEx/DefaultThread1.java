package ThreadEx;

public class DefaultThread1 {

	public static void main(String ar[]){
		
		Thread t = new Thread(){
			public void run(){
				try{
					Thread.sleep(3000);
					System.out.println("thread 종료");
				}catch(Exception e){
					
				}
			}
		};
		t.start();
		System.out.println("main 종료");
	}
	
	
}
