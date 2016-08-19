package nohup;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.http.MediaType;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class NohupControllerITTest {

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(),
			Charset.forName("utf8"));

	private HttpMessageConverter mappingJackson2HttpMessageConverter;

	@Autowired
	private WebApplicationContext ctx;

	private MockMvc mockMvc;

	@Autowired
	void setConverters(HttpMessageConverter<?>[] converters) {

		this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream().filter(
				hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().get();

		Assert.assertNotNull("the JSON message converter must not be null",
				this.mappingJackson2HttpMessageConverter);
	}

	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
	}

	@Test
	public void paramNohupNewShouldReturnOKHTTPStatus() throws Exception {

        NohupRequest request = new NohupRequest();
        request.setCommand("");
        request.setParameters(new ArrayList<>());

		this.mockMvc.perform(post("/nohup/")
				.content(this.json(request))
				.contentType(contentType))
                .andDo(print())
				.andExpect(status().isOk());
	}

    @Test
    public void paramNohupUpdateByIdShouldReturnOKHTTPStatus() throws Exception {

        NohupRequest request = new NohupRequest();
        request.setCommand("");
        request.setParameters(new ArrayList<>());

        this.mockMvc.perform(put("/nohup/toto-toto")
                .content(this.json(request))
                .contentType(contentType))
                .andDo(print())
                .andExpect(status().isOk());
    }

	@Test
	public void paramNohupGetByIdShouldReturnOKHTTPStatus() throws Exception {

		this.mockMvc.perform(get("/nohup/toto-toto"))
                .andDo(print())
				.andExpect(status().isOk());
	}

    @Test
    public void paramNohupDeleteByIdShouldReturnOKHTTPStatus() throws Exception {

        this.mockMvc.perform(delete("/nohup/toto-toto"))
                .andDo(print())
                .andExpect(status().isOk());
    }

	@Test
	public void paramNohupGetAllShouldReturnOKHTTPStatus() throws Exception {

		this.mockMvc.perform(get("/nohup/"))
				.andDo(print())
				.andExpect(status().isOk());
	}

    @Test
    public void paramNohupClearAllShouldReturnOKHTTPStatus() throws Exception {

        this.mockMvc.perform(delete("/nohup/"))
                .andDo(print())
                .andExpect(status().isOk());
    }

	@Test
	public void paramNohupKillByIdShouldReturnOKHTTPStatus() throws Exception {

		this.mockMvc.perform(get("/nohup/toto-toto/kill"))
                .andDo(print())
				.andExpect(status().isOk());
	}

    @Test
    public void paramNohupRestartByIdShouldReturnOKHTTPStatus() throws Exception {

        this.mockMvc.perform(get("/nohup/toto-toto/restart"))
                .andDo(print())
                .andExpect(status().isOk());
    }

	protected String json(Object o) throws IOException {
		MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
		this.mappingJackson2HttpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
		return mockHttpOutputMessage.getBodyAsString();
	}

}
