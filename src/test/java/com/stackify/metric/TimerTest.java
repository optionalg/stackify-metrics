/*
 * Copyright 2014 Stackify
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stackify.metric;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.stackify.metric.impl.AbstractMetric;
import com.stackify.metric.impl.Metric;
import com.stackify.metric.impl.MetricCollector;
import com.stackify.metric.impl.MetricIdentity;
import com.stackify.metric.impl.MetricMonitorType;

/**
 * Timer JUnit Test
 * @author Eric Martin
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({AbstractMetric.class, MetricManager.class})
public class TimerTest {
	
	/**
	 * testConstructor
	 */
	@Test
	public void testConstructor() {
		String category = "category";
		String name = "name";
		Timer metric = new Timer(category, name);
		
		Assert.assertEquals(category, metric.getIdentity().getCategory());
		Assert.assertEquals(name, metric.getIdentity().getName());
		Assert.assertEquals(MetricMonitorType.TIMER, metric.getIdentity().getType());
	}
	
	/**
	 * testStart
	 */
	@Test
	public void testStart() {
		MetricCollector collector = Mockito.mock(MetricCollector.class);
		PowerMockito.mockStatic(MetricManager.class);
		PowerMockito.when(MetricManager.getCollector()).thenReturn(collector);

		Timer metric = new Timer("category", "name");
		
		long value = 5;

		metric.start(new Date(System.currentTimeMillis() - (value * 1000)));
				
		ArgumentCaptor<Metric> metricCaptor = ArgumentCaptor.forClass(Metric.class);
		Mockito.verify(collector).submit(metricCaptor.capture());
				
		Assert.assertEquals(metric.getIdentity(), metricCaptor.getValue().getIdentity());
		Assert.assertEquals(value, metricCaptor.getValue().getValue(), 0.5);
		Assert.assertEquals(true, metricCaptor.getValue().isIncrement());
	}
	
	/**
	 * testStartWithNull
	 */
	@Test
	public void testStartWithNull() {
		MetricCollector collector = Mockito.mock(MetricCollector.class);
		PowerMockito.mockStatic(MetricManager.class);
		PowerMockito.when(MetricManager.getCollector()).thenReturn(collector);

		Timer metric = new Timer("category", "name");
		metric.start(null);
				
		Mockito.verifyZeroInteractions(collector);
	}
	
	/**
	 * testStartMs
	 */
	@Test
	public void testStartMs() {
		MetricCollector collector = Mockito.mock(MetricCollector.class);
		PowerMockito.mockStatic(MetricManager.class);
		PowerMockito.when(MetricManager.getCollector()).thenReturn(collector);

		Timer metric = new Timer("category", "name");
		
		long value = 5;

		metric.startMs(System.currentTimeMillis() - (value * 1000));
				
		ArgumentCaptor<Metric> metricCaptor = ArgumentCaptor.forClass(Metric.class);
		Mockito.verify(collector).submit(metricCaptor.capture());
				
		Assert.assertEquals(metric.getIdentity(), metricCaptor.getValue().getIdentity());
		Assert.assertEquals(value, metricCaptor.getValue().getValue(), 0.5);
		Assert.assertEquals(true, metricCaptor.getValue().isIncrement());
	}
	
	/**
	 * testDurationMs
	 */
	@Test
	public void testDurationMs() {
		MetricCollector collector = Mockito.mock(MetricCollector.class);
		PowerMockito.mockStatic(MetricManager.class);
		PowerMockito.when(MetricManager.getCollector()).thenReturn(collector);

		Timer metric = new Timer("category", "name");
		
		long value = 5;

		metric.durationMs(value * 1000);
				
		ArgumentCaptor<Metric> metricCaptor = ArgumentCaptor.forClass(Metric.class);
		Mockito.verify(collector).submit(metricCaptor.capture());
				
		Assert.assertEquals(metric.getIdentity(), metricCaptor.getValue().getIdentity());
		Assert.assertEquals(value, metricCaptor.getValue().getValue(), 0.5);
		Assert.assertEquals(true, metricCaptor.getValue().isIncrement());
	}
	
	/**
	 * testAutoReportZeroValue
	 */
	@Test
	public void testAutoReportZeroValue() {
		MetricCollector collector = Mockito.mock(MetricCollector.class);
		PowerMockito.mockStatic(MetricManager.class);
		PowerMockito.when(MetricManager.getCollector()).thenReturn(collector);

		Timer metric = new Timer("category", "name");
		
		metric.autoReportZeroValue();
				
		ArgumentCaptor<MetricIdentity> metricIdentityCaptor = ArgumentCaptor.forClass(MetricIdentity.class);
		Mockito.verify(collector).autoReportZero(metricIdentityCaptor.capture());
				
		Assert.assertEquals(metric.getIdentity(), metricIdentityCaptor.getValue());
	}
}
