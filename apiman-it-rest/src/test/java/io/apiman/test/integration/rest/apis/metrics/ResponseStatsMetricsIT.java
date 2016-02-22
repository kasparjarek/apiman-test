/*
 * Copyright 2016 Red Hat Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.apiman.test.integration.rest.apis.metrics;

import static org.junit.Assert.assertEquals;

import io.apiman.manager.api.beans.metrics.HistogramBean;
import io.apiman.manager.api.beans.metrics.HistogramDataPoint;
import io.apiman.manager.api.beans.metrics.HistogramIntervalType;
import io.apiman.manager.api.beans.metrics.ResponseStatsDataPoint;
import io.apiman.manager.api.beans.metrics.ResponseStatsHistogramBean;
import io.apiman.manager.api.beans.metrics.ResponseStatsSummaryBean;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

/**
 * @author jkaspar
 */
public class ResponseStatsMetricsIT extends AbstractIntervalMetricsIT {

    @SuppressWarnings("unchecked")
    @Override public HistogramBean<HistogramDataPoint> getMetrics() {
        return (HistogramBean) apiVersions.metricsResponseStats(beforeRecoding, tenMinutesAfterRecording,
                HistogramIntervalType.minute);
    }

    @Test
    public void valuesInEachSubintervalAreCorrect() throws Exception {
        recordMetricsIntoNextMinute();

        ResponseStatsHistogramBean metrics = apiVersions.metricsResponseStats(beforeRecoding, tenMinutesAfterRecording,
                HistogramIntervalType.minute);

        for (ResponseStatsDataPoint i : metrics.getData()) {
            Date subinterval = formatter.parse(i.getLabel());

            calendar.setTime(subinterval);
            calendar.add(Calendar.SECOND, 59);
            Date endOfSubinterval = calendar.getTime();
            ResponseStatsSummaryBean expectedMetrics = apiVersions.metricsSummaryResponseStats(subinterval, endOfSubinterval);

            assertEquals("Unexpected count value", expectedMetrics.getTotal(), i.getTotal());
            assertEquals("Unexpected count value", expectedMetrics.getFailures(), i.getFailures());
            assertEquals("Unexpected count value", expectedMetrics.getErrors(), i.getErrors());
        }
    }
}