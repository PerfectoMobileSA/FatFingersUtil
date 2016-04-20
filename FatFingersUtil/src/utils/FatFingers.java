package utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import utils.Constants;

/**
 * The Class FatFingerTest.
 */
public class FatFingers  {
	
	private RemoteWebDriver driver;
	
	public int resolutionWidth;
	public int resolutionHeight;
	
	
	/** The rand. */
	private Random rand;
	
	/** The rand helper. */
	private Random randHelper;

	/** The center of element. */
	Point LeftUpperOfElement;
	
	/** The element dimension. */
	Dimension elementDimension;
	
	/** The max y. */
	int minX, minY,maxX,maxY;
	
	/** The new point. */
	Point newPoint;
	
	/** The pixels to add. */
	int pixelsToAdd;
	

	/**
	 * Instantiates a new fat finger test.
	 *
	 * @param driver the driver
	 */
	public FatFingers(RemoteWebDriver driver) {
		
		//initialize:
		this.driver = driver;
		this.resolutionHeight = 0;
		this.resolutionWidth = 0;
		
		//get  device resolution:
		setResolution();
	
	}
	
	
	public void click(WebElement element){
		this.click(element, Constants.defaultFatFingers);
	}
	/**
	 * Click.
	 *
	 * @param element the element
	 * @param pixels the pixels
	 */
	public boolean click(WebElement element, int pixels){
		Map<String, Object> params = new HashMap<>();	
		try {
			
			this.pixelsToAdd = pixels;
			this.newPoint = null;
			LeftUpperOfElement = element.getLocation();
			elementDimension = element.getSize();
			
			minX = LeftUpperOfElement.x;
			maxX = minX + elementDimension.width;
			minY = LeftUpperOfElement.y;
			maxY = minY + elementDimension.height;
			
			//set new point relying the current element dimension:
			setNewPoint();
			
			//click on new point:
			params.put("location", this.newPoint.x + "," + this.newPoint.y);
			driver.executeScript("mobile:touch:tap", params);
			
			System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
			System.out.println("Screen Resolution: "+resolutionWidth + "x" + resolutionHeight);
			System.out.println("Original Element: Location="+ LeftUpperOfElement.toString()+",Dimension="+elementDimension.toString());
			System.out.println("FAT FINGER: "+pixels+ " Pixels");
			System.out.println("Clicked Fat Finger Point= "+newPoint.toString());
			System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
			return true;


		} catch (Exception e) {
			System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
			System.out.println("Screen Resolution: "+resolutionWidth + "x" + resolutionHeight);
			System.out.println("Original Element: Location="+ LeftUpperOfElement.toString()+",Dimension="+elementDimension.toString());
			System.out.println("FAT FINGER: "+pixels+ " Pixels");
			System.out.println("FAILED to Click Fat Finger Point= "+newPoint.toString());
			System.out.println("Exception"+ e.getMessage());
			System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
			return false;
		}  
	
		
	}
	
		/**
		 * Sets the new point.
		 */
		private void setNewPoint(){
			Constants.side side = Constants.side.getRandomSide();
			Random rand = new Random();
			int newX=0;
			int newY=0;
			
			switch (side){
				
				case UP:
					newX = rand.nextInt(this.maxX - this.minX + 1) + this.minX;
					newY = Math.max(minY - pixelsToAdd , 1);
				break;
				case DOWN:
					newX = rand.nextInt(this.maxX - this.minX + 1) + this.minX;
					newY = Math.min(maxY + pixelsToAdd , this.resolutionHeight-1);
				break;
				case LEFT:
					newY = rand.nextInt(this.maxY - this.minY + 1) + this.minY;
					newX = Math.max(minX - pixelsToAdd , 1);
				break;
				case RIGHT:
					newY = rand.nextInt(this.maxY - this.minY + 1) + this.minY;
					newX = Math.min(maxX + pixelsToAdd , this.resolutionWidth-1);
				break;
				
			}
			
			newPoint = new Point(newX,newY);
		}
		

				
		public void setResolution(){
			Map<String, Object> params = new HashMap<>();
			params.put("property", "resolution");
			String resolution = (String) driver.executeScript("mobile:handset:info", params);
			if (!resolution.isEmpty()){
				try {
					String[] parts = resolution.split("\\*");
					this.resolutionWidth = Integer.valueOf(parts[0]);
					this.resolutionHeight = Integer.valueOf(parts[1]);
					
					return;
				} catch (Exception e) {
					System.out.println(e.getMessage());
					System.out.println("couldnt get resolution, try something else");
				}
				
			}
			

		   params.put("property", "resolutionWidth");
		   resolutionWidth = Integer.parseInt((String) driver.executeScript("mobile:handset:info", params));
		   params.put("property", "resolutionHeight");
		   resolutionHeight = Integer.parseInt((String) driver.executeScript("mobile:handset:info", params));
		
	     }
}

		
		
	

