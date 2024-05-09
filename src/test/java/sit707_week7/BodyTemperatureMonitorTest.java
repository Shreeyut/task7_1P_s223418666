package sit707_week7;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import sit707_week7.sample.StudentRepository;

public class BodyTemperatureMonitorTest {

	@Test
	public void testStudentIdentity() {
		String studentId = "s223418666";
		Assert.assertNotNull("Student ID is null", studentId);
	}

	@Test
	public void testStudentName() {
		String studentName = "Shreeyut Shrestha";
		Assert.assertNotNull("Student name is null", studentName);
	}
	
	@Test
	public void testReadTemperatureNegative() {
		TemperatureSensor temps = Mockito.mock(TemperatureSensor.class);
		CloudService cloud = Mockito.mock(CloudService.class);
		NotificationSender notif = Mockito.mock(NotificationSender.class);
		Mockito.when(temps.readTemperatureValue()).thenReturn((double)-5);
		BodyTemperatureMonitor body = new BodyTemperatureMonitor(temps,cloud,notif);
		Assert.assertEquals((double)-5, body.readTemperature(), 0.1);
	}
	
	@Test
	public void testReadTemperatureZero() {
		TemperatureSensor temps = Mockito.mock(TemperatureSensor.class);
		CloudService cloud = Mockito.mock(CloudService.class);
		NotificationSender notif = Mockito.mock(NotificationSender.class);
		Mockito.when(temps.readTemperatureValue()).thenReturn((double)0);
		BodyTemperatureMonitor body = new BodyTemperatureMonitor(temps,cloud,notif);
		Assert.assertEquals((double)0, body.readTemperature(), 0.1);
	}
	
	@Test
	public void testReadTemperatureNormal() {
		TemperatureSensor temps = Mockito.mock(TemperatureSensor.class);
		CloudService cloud = Mockito.mock(CloudService.class);
		NotificationSender notif = Mockito.mock(NotificationSender.class);
		Mockito.when(temps.readTemperatureValue()).thenReturn((double)36);
		BodyTemperatureMonitor body = new BodyTemperatureMonitor(temps,cloud,notif);
		Assert.assertEquals((double)36, body.readTemperature(), 0.1);
	}

	@Test
	public void testReadTemperatureAbnormallyHigh() {
		TemperatureSensor temps = Mockito.mock(TemperatureSensor.class);
		CloudService cloud = Mockito.mock(CloudService.class);
		NotificationSender notif = Mockito.mock(NotificationSender.class);
		Mockito.when(temps.readTemperatureValue()).thenReturn((double)99);
		BodyTemperatureMonitor body = new BodyTemperatureMonitor(temps,cloud,notif);
		Assert.assertEquals((double)99, body.readTemperature(), 0.1);
	}

	
	@Test
	public void testReportTemperatureReadingToCloud() {
		// Mock reportTemperatureReadingToCloud() calls cloudService.sendTemperatureToCloud()
		TemperatureSensor temps = Mockito.mock(TemperatureSensor.class);
		CloudService cloud = Mockito.mock(CloudService.class);
		NotificationSender notif = Mockito.mock(NotificationSender.class);
		BodyTemperatureMonitor body = new BodyTemperatureMonitor(temps,cloud,notif);
		TemperatureReading reads= new TemperatureReading();
		body.reportTemperatureReadingToCloud(reads);
		Mockito.verify(cloud).sendTemperatureToCloud(reads);			
	}
	
	
	
	@Test
	public void testInquireBodyStatusNormalNotification() {
		TemperatureSensor temps = Mockito.mock(TemperatureSensor.class);
		CloudService cloud = Mockito.mock(CloudService.class);
		NotificationSender notif = Mockito.mock(NotificationSender.class);
		BodyTemperatureMonitor body = new BodyTemperatureMonitor(temps,cloud,notif);
		Customer cust = body.getFixedCustomer();
		Mockito.when(cloud.queryCustomerBodyStatus(cust)).thenReturn("NORMAL");
		body.inquireBodyStatus();
		Mockito.verify(notif).sendEmailNotification(cust,"Thumbs Up!");		
	}
	
	
	@Test
	public void testInquireBodyStatusAbnormalNotification() {
		TemperatureSensor temps = Mockito.mock(TemperatureSensor.class);
		CloudService cloud = Mockito.mock(CloudService.class);
		NotificationSender notif = Mockito.mock(NotificationSender.class);
		BodyTemperatureMonitor body = new BodyTemperatureMonitor(temps,cloud,notif);
		Customer cust = body.getFixedCustomer();
		FamilyDoctor doct = body.getFamilyDoctor();
		Mockito.when(cloud.queryCustomerBodyStatus(cust)).thenReturn("ABNORMAL");
		body.inquireBodyStatus();
		Mockito.verify(notif).sendEmailNotification(doct,"Emergency!");
	}
}
