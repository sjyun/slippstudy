import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.StandardWatchEventKinds;

public class WatchServiceTest {

	public static void main(String[] args) throws Exception{
		//FWatcher fileCheck = this.new FWatcher(args[0]);
		WatchServiceTest watch= new WatchServiceTest();
		watch.startFwatch("d:/00.work_dir");
	}
	
	public void startFwatch(String path)throws InterruptedException{
		FWatcher fileCheck  = new FWatcher(path);
		Thread t = new Thread(fileCheck);
		t.start();
		t.join();
	}
	
	
	private class FWatcher implements Runnable{
		String path;
		public FWatcher(String path) {
			this.path = path;
		}
		@Override
		public void run() {
			while(true){
				System.out.println("run");
				handleDirectoryChangeEvent(path);
			}
		}
	}
	
	 private static void handleDirectoryChangeEvent(String path) {
		  try {
			  System.out.println("sss");
		   Path dir = Paths.get(path);
		   WatchService watchService = FileSystems.getDefault().newWatchService();
		   dir.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);
		   WatchKey watchKey = watchService.take();
		   for (WatchEvent<?> event : watchKey.pollEvents()) {
			final WatchEvent<Path> ev = (WatchEvent<Path>) event;
		    final Path name = ev.context();
		    final Path child = dir.resolve(name);
		    final Kind<?> kind = event.kind();
		    //event 처리
		    
		    if( kind == StandardWatchEventKinds.OVERFLOW ){
		    	continue;
		    }
		    
		    if( kind == StandardWatchEventKinds.ENTRY_CREATE ){
		    	//final Path directory_path = director
		    	  //System.out.println("Created: " + object.context().toString());
		    	//System.out.println("Created: " +  ((Path) event).toRealPath(true)+"/"+ ((WatchEvent<?>) kind).context().toString());
		    }
		    
		    
		    System.out.format("%s: %s\n", event.kind().name(), child);
		   }
		        } catch (Exception x) {
		            return;
		        }
		 }
}
