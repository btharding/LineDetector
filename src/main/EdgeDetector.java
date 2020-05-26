package main;

import java.awt.image.BufferedImage;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class EdgeDetector {
	
	private float[][] filter;
	private Color[][] pixArray;
	
	public Image filterImage(Image image) {
		
		filter = createFilter();
		pixArray = getPixelDataExtended(image);
		pixArray = applyGreyscale(pixArray);
		pixArray = applyFilter(pixArray, filter);
		pixArray = trimPixelData(pixArray);
		
		return createImage(pixArray);
		
	}
	
	public float[][] createFilter(){
		
		float[][] filter = {{-1.0f,-1.0f,-1.0f},{-1.0f,8.0f,-1.0f},{-1.0f,-1.0f,-1.0f}};
		
		return filter;
		
	}
	
	public Color[][] getPixelData(Image image){
		//Takes image, converts to pixel array
		
		BufferedImage buffer = SwingFXUtils.fromFXImage(image, null);
		Color[][] pixelArray = new Color[(int)image.getHeight()][(int)image.getWidth()];
		
		for(int y = 0; y<(int)image.getHeight(); y++) {
			
			for(int x = 0; x<(int)image.getWidth(); x++) {
				
				int rgbval = buffer.getRGB(x,y);
				
				float r = (rgbval>>16) & 0xff;
				r = r/255;
				
				float g = (rgbval>>8) & 0xff;
				g = g/255;
				
				float b = rgbval & 0xff;
				b = b/255;
				
				pixelArray[y][x] = new Color(r,g,b,1.0);
				
			}
			
		}
		
		return pixelArray;
		
	}
	
	public Color[][] getPixelDataExtended(Image image){
		//Takes image, converts to pixel array with border
		
		Color[][] pixelArray = getPixelData(image);
		
		Color[][] extendedPixelArray = new Color[(int)image.getHeight()+2][(int)image.getWidth()+2];		
		extendedPixelArray = setToBlack(extendedPixelArray);
				
		
		for(int row = 0; row<pixelArray.length;row++) {
			
			for(int col = 0; col<pixelArray[0].length;col++) {
				
				extendedPixelArray[row+1][col+1] = pixelArray[row][col];
				
			}
			
		}
		
		return extendedPixelArray;
		
	}

	public Color[][] applyGreyscale(Color[][] pixels){
		//Takes pixel array, converts to pixel array with avg colours (greyscale)
		
		Color[][] greyscaleArray = new Color[pixels.length][pixels[0].length];
		
		for(int row = 0; row<greyscaleArray.length;row++) {
			
			for(int col = 0; col<greyscaleArray[0].length;col++) {
				
				Color p = pixels[row][col];
				
				float r = (float)p.getRed();
				float g = (float)p.getGreen();
				float b = (float)p.getBlue();
				
				float avg = (r+g+b)/3;
				
				greyscaleArray[row][col] = new Color(avg,avg,avg,1);
				
			}
			
		}
		
		return greyscaleArray;
		
	}
	
	public Color[][] applyFilter(Color[][] pixels , float[][] filter){
		
		Color[][] filteredArray = new Color[pixels.length][pixels[0].length];
		filteredArray = setToBlack(filteredArray);
		
		for(int row = 1; row<filteredArray.length-1; row++) {
			
			for(int col = 1; col<filteredArray[0].length-1;col++) {
				
				float r = (float)((pixels[row-1][col-1].getBlue()*filter[0][0])+
						(pixels[row-1][col].getBlue()*filter[0][1])+
						(pixels[row-1][col+1].getBlue()*filter[0][2])+
						(pixels[row][col-1].getBlue()*filter[1][0])+
						(pixels[row][col].getBlue()*filter[1][1])+
						(pixels[row][col+1].getBlue()*filter[1][2])+
						(pixels[row+1][col-1].getBlue()*filter[2][0])+
						(pixels[row+1][col].getBlue()*filter[2][1])+
						(pixels[row+1][col+1].getBlue()*filter[2][2]));
				
				if(r>1) {
					r=1;
				}else if(r<0) {
					r=0;
				}
				
				filteredArray[row][col] = new Color(r,r,r,1);
				
			}
			
		}
		
		return filteredArray;
		
	}
	
	public Image createImage(Color[][] pixels) {
		//takes pixel array and returns it's image
		
		WritableImage wimg = new WritableImage(pixels[0].length, pixels.length);
		PixelWriter pw = wimg.getPixelWriter();
		
		for (int row = 0; row < pixels.length; row++) {
			
			for (int col = 0; col < pixels[0].length; col++) {
				
				pw.setColor(col, row, pixels[row][col]);
				
			}
			
		}
		
		BufferedImage bImage = SwingFXUtils.fromFXImage(wimg, null);
		
		return SwingFXUtils.toFXImage(bImage, null );
		
	}
	
	public Color[][] setToBlack(Color[][] pixels){
		
		for(int row = 0; row<pixels.length; row++) {
			
			for(int col = 0; col<pixels[0].length; col++) {
				
				pixels[row][col] = new Color(0,0,0,1);
				
			}
			
		}
		
		return pixels;
		
	}

	public Color[][] trimPixelData(Color[][] pixels){
		//Takes pixel array, removes first and last row and column
		
		Color[][] trimmedPixelArray = new Color[pixels.length-2][pixels[0].length-2];
		
		for(int row = 0; row<trimmedPixelArray.length; row++) {
			
			for(int col = 0; col<trimmedPixelArray[0].length;col++) {
				
				trimmedPixelArray[row][col] = pixels[row+1][col+1];
				
			}
			
		}
		
		return trimmedPixelArray;
		
	}
	
}
