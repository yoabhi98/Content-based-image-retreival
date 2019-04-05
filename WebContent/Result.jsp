<%@ page language="java" import="java.io.File;" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<%
 File f0 = new File(imagePaths[0]);
        resizeToFitJlabel(f0, jLabelRT1);
        File f1 = new File(imagePaths[1]);
        resizeToFitJlabel(f1, myLabelRT2);
        File f2 = new File(imagePaths[2]);
        resizeToFitJlabel(f2, myLabelRT3);
        File f3 = new File(imagePaths[3]);
        resizeToFitJlabel(f3, myLabelRT4);
        File f4 = new File(imagePaths[4]);
        resizeToFitJlabel(f4, myLabelRT5);
        
        public void resizeToFitJlabel(File imageToBeResized, JLabel imageJLabel) 
        {
            int labelwidth = imageJLabel.getWidth();
            int labelheight = imageJLabel.getHeight();
            ImageIcon Icon = new ImageIcon(imageToBeResized.getAbsolutePath());
            Image img1 = Icon.getImage();
            imageJLabel.setIcon(new ImageIcon(img1.getScaledInstance(labelwidth, labelheight, Image.SCALE_SMOOTH)));
        }
%>
</body>
</html>