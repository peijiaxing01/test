

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.im4java.core.CompositeCmd;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;


public class ImageUtil {

    public static String graphicsMagicPath = "D:/buyv5/GraphicsMagick-1.3.23-Q8";

    /**
     * * 根据坐标裁剪图片
     * 
     * @param srcPath
     *            要裁剪图片的路径
     * @param newPath
     *            裁剪图片后的路径
     * @param x
     *            起始横坐标
     * @param y
     *            起始挫坐标
     * @param x1
     *            结束横坐标
     * @param y1
     *            结束挫坐标
     */
    public static void cutImage(String srcPath, String newPath, int x, int y,
            int x1, int y1) throws Exception {
        int width = x1 - x;
        int height = y1 - y;
        IMOperation op = new IMOperation();
        op.addImage(srcPath);
        /**
         * width：裁剪的宽度 height：裁剪的高度 x：裁剪的横坐标 y：裁剪的挫坐标
         */
        op.crop(width, height, x, y);
        op.addImage(newPath);
        ConvertCmd convert = new ConvertCmd(true);
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.indexOf("win") != -1) {
            // linux下不要设置此值，不然会报错
            convert.setSearchPath(graphicsMagicPath);
        }
        convert.run(op);
    }

    /**
     * 根据尺寸缩放图片
     * 
     * @param width
     *            缩放后的图片宽度
     * @param height
     *            缩放后的图片高度
     * @param srcPath
     *            源图片路径
     * @param newPath
     *            缩放后图片的路径
     * @param type
     *            1为像素，2为比例，如（大小：1024x1024,比例：50%x50%）
     */
    public static String cutImage(int width, int height, String srcPath,
            String newPath, int type, String quality) throws Exception {
        IMOperation op = new IMOperation();
        ConvertCmd cmd = new ConvertCmd(true);
        op.addImage();
        String raw = "";
        if (type == 1) {
            // 按像素
            raw = width + "x" + height+"^";
        } else {
            // 按像素百分比
            raw = width + "%x" + height + "%";
        }
        op.addRawArgs("-resize" ,  raw );
        op.addRawArgs("-gravity", "center");
        if ((quality != null && quality.equals(""))) {
            op.addRawArgs("-quality", quality);
        }
        op.addImage();

        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.indexOf("win") != -1) {
            // linux下不要设置此值，不然会报错
            cmd.setSearchPath(graphicsMagicPath);
        }

        try {
            cmd.run(op, srcPath, newPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newPath;
    }

    /**
     * 先缩放，后居中切割图片
     * 
     * @param srcPath
     *            源图路径
     * @param desPath
     *            目标图保存路径
     * @param rectw
     *            待切割在宽度
     * @param recth
     *            待切割在高度
     */
    public static void cropImageCenter(String srcPath, String desPath,
            int width) {
        File tofile = new File(desPath);
        if (!tofile.getParentFile().exists()) {
            tofile.getParentFile().mkdirs();
        }
        /** 压缩*/
        ImageModel model = getImageSizeByImageReader(srcPath);
        int cutWidth = 0;
        int cutHeight = 0;
        if (model.getWidth() < model.getHeight()) {
            if (model.getWidth() < width) {
                cutWidth = model.getWidth();
            } else {
                cutWidth = width;
            }
        } else if (model.getWidth() >= model.getHeight()) {
            if (model.getHeight() < width) {
                cutHeight = model.getHeight();
            } else {
                cutHeight = width;
            }
        }
        
        IMOperation op = new IMOperation();
        op.addImage();
        op.resize(cutWidth, cutHeight, "^").gravity("center"); //.extent(rectw, recth);
        op.addImage();
        ConvertCmd convert = new ConvertCmd(true);
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.indexOf("win") != -1) {
            // linux下不要设置此值，不然会报错
            convert.setSearchPath(graphicsMagicPath);
        }
        try {
            convert.run(op, srcPath, desPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        /** 剪裁*/
        int rectWidth = 0;
        int rectHeight = 0;
        model = getImageSizeByImageReader(desPath);
        if (model.getWidth() < width) {
            rectWidth = model.getWidth();
        } else {
            rectWidth = width;
        }
        
        if (model.getHeight() < width) {
            rectHeight = model.getHeight();
        } else {
            rectHeight = width;
        }
        IMOperation op1 = new IMOperation();
        op1.addImage();
        op1.gravity("center").extent(rectWidth, rectHeight);
        op1.addImage();
        try {
            convert.run(op1, desPath, desPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    /**
     * 给图片加水印
     * 
     * @param srcPath
     *            源图片路径
     */
    public static void addImgText(String srcPath) throws Exception {
        IMOperation op = new IMOperation();
        op.font("Arial").gravity("southeast").pointsize(18).fill("#BCBFC8")
                .draw("text 100,100 co188.com");
        op.addImage();
        op.addImage();

        ConvertCmd cmd = new ConvertCmd(true);
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.indexOf("win") != -1) {
            // linux下不要设置此值，不然会报错
            cmd.setSearchPath(graphicsMagicPath);
        }

        try {
            cmd.run(op, srcPath, srcPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 使用ImageReader获取图片尺寸
     * 
     * @param src
     *            源图片路径
     */
    public static ImageModel getImageSizeByImageReader(String src) {
        String []srcArray = src.split("\\.");
        File file = new File(src);
        try {
            Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName(srcArray[1]);
            ImageReader reader = (ImageReader) readers.next();
            ImageInputStream iis = ImageIO.createImageInputStream(file);
            reader.setInput(iis, true);
            ImageModel model = new ImageModel();
            model.setWidth(reader.getWidth(0));
            model.setHeight(reader.getHeight(0));
            model.setSize(file.length());
            iis.close();
            return model;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 按给定的像素宽度压缩图片
     * @param width 给定的压缩宽度
     * 
     * @param srcPath 源图路径
     */
    public static void cutImage(int width, String srcPath) {
        ImageModel model = getImageSizeByImageReader(srcPath);
        if (model.getWidth() > width) { 
            try {
                cutImage(width, 0, srcPath, srcPath, 1, "100");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
    }
    
    /**
     * 按九宫格位置添加水印
     * 
     * @param srcPath
     *            原图片路径
     * @param distPath
     *            新图片路径
     * @param watermarkImg
     *            水印图片路径
     * @param position
     *            九宫格位置[1-9],从上往下,从左到右排序
     * @param x
     *            横向边距
     * @param y
     *            纵向边距
     * @param alpha
     *            透明度
     * @throws IOException
     * @throws InterruptedException
     * @throws IM4JavaException
     */
    public static void WatermarkImg(String srcPath, String distPath,
            String watermarkImg, int position, int x, int y, int alpha)
            throws IOException, InterruptedException, IM4JavaException {
        int[] watermarkImgSide = getImageSide(watermarkImg);
        int[] srcImgSide = getImageSide(srcPath);
        int[] xy = getXY(srcImgSide, watermarkImgSide, position, y, x);
        watermarkImg(srcPath, distPath, watermarkImg, watermarkImgSide[0],
                watermarkImgSide[1], xy[0], xy[1], alpha);
    }

    private static int[] getImageSide(String imgPath) throws IOException {
        int[] side = new int[2];
        Image img = ImageIO.read(new File(imgPath));
        side[0] = img.getWidth(null);
        side[1] = img.getHeight(null);
        return side;
    }

    /**
     * 添加图片水印
     * 
     * @param srcPath
     *            原图片路径
     * @param distPath
     *            新图片路径
     * @param watermarkImg
     *            水印图片路径
     * @param width
     *            水印宽度（可以于水印图片大小不同）
     * @param height
     *            水印高度（可以于水印图片大小不同）
     * @param x
     *            水印开始X坐标
     * @param y
     *            水印开始Y坐标
     * @param alpha
     *            透明度[0-100]
     * @throws IOException
     * @throws InterruptedException
     * @throws IM4JavaException
     */
    private static void watermarkImg(String srcPath, String distPath,
            String watermarkImg, int width, int height, int x, int y, int alpha)
            throws IOException, InterruptedException, IM4JavaException {
        CompositeCmd cmd = new CompositeCmd(true);
        String path = graphicsMagicPath;
        cmd.setSearchPath(path);
        IMOperation op = new IMOperation();
        op.dissolve(alpha);
        op.geometry(width, height, x, y);
        op.addImage(watermarkImg);
        op.addImage(srcPath);
        op.addImage(distPath);
        cmd.run(op);
    }

    private static int[] getXY(int[] image, int[] watermark, int position, int x, int y) {
        int[] xy = new int[2];
        if (position == 1) {
            xy[0] = x;
            xy[1] = y;
        } else if (position == 2) {
            xy[0] = (image[0] - watermark[0]) / 2; // 横向边距
            xy[1] = y; // 纵向边距
        } else if (position == 3) {
            xy[0] = image[0] - watermark[0] - x;
            xy[1] = y;
        } else if (position == 4) {
            xy[0] = x;
            xy[1] = (image[1] - watermark[1]) / 2;
        } else if (position == 5) {
            xy[0] = (image[0] - watermark[0]) / 2;
            xy[1] = (image[1] - watermark[1]) / 2;
        } else if (position == 6) {
            xy[0] = image[0] - watermark[0] - x;
            xy[1] = (image[1] - watermark[1]) / 2;
        } else if (position == 7) {
            xy[0] = x;
            xy[1] = image[1] - watermark[1] - y;
        } else if (position == 8) {
            xy[0] = (image[0] - watermark[0]) / 2;
            xy[1] = image[1] - watermark[1] - y;
        } else {
            xy[0] = image[0] - watermark[0] - x;
            xy[1] = image[1] - watermark[1] - y;
        }
        return xy;
    }

    /**
     * 把文字转化为一张背景透明的png图片
     * 
     * @param str
     *            文字的内容
     * @param fontType
     *            字体，例如宋体
     * @param fontSize
     *            字体大小
     * @param colorStr
     *            字体颜色，不带#号，例如"990033"
     * @param outfile
     *            png图片的路径
     * @throws Exception
     */
    public static void converFontToImage(String str, String fontType, int fontSize,
            String colorStr, String outfile) throws Exception {

        Font font = new Font(fontType, Font.BOLD, fontSize);
        File file = new File(outfile);
        // 获取font的样式应用在str上的整个矩形
        Rectangle2D r = font.getStringBounds(str, new FontRenderContext(
                AffineTransform.getScaleInstance(1, 1), false, false));
        int unitHeight = (int) Math.floor(r.getHeight());// 获取单个字符的高度
        // 获取整个str用了font样式的宽度这里用四舍五入后+1保证宽度绝对能容纳这个字符串作为图片的宽度
        int width = (int) Math.round(r.getWidth()) + 1;
        int height = unitHeight + 3;// 把单个字符的高度+3保证高度绝对能容纳字符串作为图片的高度
        // 创建图片

        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        image = g2d.getDeviceConfiguration().createCompatibleImage(width,
                height, Transparency.TRANSLUCENT);
        g2d.dispose();
        g2d = image.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(1));
        g2d.setColor(new Color(Integer.parseInt(colorStr, 16)));// 在换成所需要的字体颜色
        g2d.setFont(font);
        g2d.drawString(str, 0, font.getSize());

        ImageIO.write(image, "png", file);// 输出png图片
    }
    
    
    /**
     * 旋转图片
     * 
     * @param imgPath
     *            源图路径
     */
    public static void rotateImage(String imgPath, int width) {
        /** 原图旋转：旋转后按宽度的进行压缩*/
        ImageModel model = getImageSizeByImageReader(imgPath);
        int cutWidth = 0;
        int cutHeight = 0; 
        if (model.getHeight() > width) {
            cutWidth = width;
        } else {
            cutWidth = model.getHeight();
        }
        IMOperation op = new IMOperation();
        op.addImage();
        op.rotate(90d); //.extent(rectw, recth);
        op.addRawArgs("-quality", "100");
        op.resize(cutWidth, cutHeight, "^").gravity("center");
        op.addRawArgs("-quality", "100");
        op.addImage();
        ConvertCmd convert = new ConvertCmd(true);
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.indexOf("win") != -1) {
            // linux下不要设置此值，不然会报错
            convert.setSearchPath(graphicsMagicPath);
        }
        try {
            convert.run(op, imgPath, imgPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    

    public static void main(String[] args) throws Exception {
        Long start = System.currentTimeMillis();
        //裁剪图片
        //cutImage("D:/buyv5/images/1/20160112212710081_Tulips.jpg", "D:/buyv5/images/1/20160112212710081_TulipsCT.jpg",98, 48, 370, 320);
        
        //缩放图片
        //cutImage(100, "D:/buyv5/images/1/20160112212710081_Tulips.jpg");
        //cutImage(1400,700, "D:/buyv5/images/1/20160112212710081_TulipsW.jpg","D:/buyv5/images/1/20160112212710081_TulipsEX.jpg",1,"100");
        
        //添加水印
        //addImgText("D:/buyv5/images/1/20160112212710081_Tuli ps.jpg");
        //WatermarkImg("D:/buyv5/images/1/org/20160118214852222_Tulips.jpg", "D:/buyv5/images/1/org/20160118214852222_Tulips_1.jpg",
        //        "D:/buyv5/images/logo2.png", 9, 30, 30, 100);
        
        for (int i=0 ; i< 100 ; i++){
            rotateImage("D:/buyv5/images/1/org/pic_12_1.jpg" , 280);
            System.out.println(i);
        }
        // 先缩放后切割
        //cropImageCenter("D:/buyv5/images/1/20160112212710081_Tulips.jpg", "D:/buyv5/images/1/20160112212710081_TulipsEX.jpg", 380);
        //cropImageCenter("D:/buyv5/images/1/20160112212710081_TulipsW.jpg", "D:/buyv5/images/1/20160112212710081_TulipsEX.jpg", 1000,1000);
        //ImageModel model = getImageSizeByImageReader("D:/buyv5/images/1/20160112212710081_TulipsEX.jpg");
        //System.out.println(model.getWidth() + "  " + model.getHeight());
        //Long end = System.currentTimeMillis();
        //System.out.println("time:"+(end-start)/3600);
    }
}
