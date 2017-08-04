package nohup.services;

import nohup.model.NohupMonitoringTrace;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by mibaye on 04/08/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-context.xml"})
public class NohupServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(NohupMonitoring.class);

    @Autowired
    private ElasticsearchTemplate template;

    @Autowired
    private NohupService service;

    @Before
    public void before() {
        template.deleteIndex(NohupMonitoringTrace.class);
        template.createIndex(NohupMonitoringTrace.class);
        template.putMapping(NohupMonitoringTrace.class);
    }

    @Test
    public void testNohupServiceIsNotNull() throws Exception {
        Assert.assertNotNull(service);
    }
}
