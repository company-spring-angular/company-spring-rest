
package com.company.rest.controller;

import com.company.dao.CompanyDAO;
import com.company.model.Company;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

//import static org.hamcrest.Matchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {TestContext.class, TestRestServiceWebConfig.class})
@WebAppConfiguration
public class CompanyControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON_UTF8.getType(),
            MediaType.APPLICATION_JSON_UTF8.getSubtype(), Charset.forName("utf8"));

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private CompanyDAO companyDAOMock;

    private MockMvc mockMvc;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().get();

        Assertions.assertThat(mappingJackson2HttpMessageConverter).isNotNull();
    }

    @Before
    public void before() {
        Mockito.reset(companyDAOMock);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void companyCreate() throws Exception {
        Company company = CompanyTestData.createCompany();
        String companyJson = json(company);

        when(companyDAOMock.save(any(Company.class))).thenReturn(company);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/v1/companies")
                        .contentType(contentType)
                        .content(companyJson))
                .andExpect(MockMvcResultMatchers.status()
                        .isCreated())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.content().json(companyJson));

        verify(companyDAOMock, times(1)).save(any(Company.class));
        verifyNoMoreInteractions(companyDAOMock);
    }

    @Test
    public void companyCreateWithoutNameShouldRespondWithBadRequestCode() throws Exception {
        Company company = CompanyTestData.createCompany();
        company.setName(null);
        String companyJson = json(company);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/v1/companies")
                        .contentType(contentType)
                        .content(companyJson))
                .andExpect(MockMvcResultMatchers.status()
                        .isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cause")
                        .value("Company 'name' property cannot be null nor empty"))
        ;
    }

    @Test
    public void createCompanyWithEmptyRequestShouldRespondWithInvalidRequest() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post("/v1/companies")
                        .contentType(contentType))
                .andExpect(MockMvcResultMatchers.status()
                        .isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cause")
                        .value("Invalid request"))
        ;
    }

    @Test
    public void companyListShouldRespondWithValidResponse() throws Exception {
        String id1 = RandomStringUtils.randomNumeric(3);
        String id2 = RandomStringUtils.randomNumeric(3);
        Company company_1 = CompanyTestData.createCompany();
        Company company_2 = CompanyTestData.createCompany();

        company_1.setCid(id1);
        company_2.setCid(id2);

        when(companyDAOMock.findAll()).thenReturn(Arrays.asList(company_1, company_2));

        mockMvc.perform(
                MockMvcRequestBuilders.get("/v1/companies"))
                .andExpect(MockMvcResultMatchers.status()
                        .isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].cid")
                        .value(id1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].cid")
                        .value(id2));

        verify(companyDAOMock, times(1)).findAll();
        verifyNoMoreInteractions(companyDAOMock);
    }

    @Test
    public void companyFindShouldRespondWithCompanyNotFoundCode() throws Exception {
        String companyId = RandomStringUtils.randomNumeric(3);

        when(companyDAOMock.findById(anyString())).thenReturn(null);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/v1/companies/" + companyId))
                .andExpect(MockMvcResultMatchers.status()
                        .isNotFound())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cause")
                        .value("Company not found"));

        verify(companyDAOMock, times(1)).findById(anyString());
        verifyNoMoreInteractions(companyDAOMock);
    }

    @Test
    public void companyFindWithInvalidIdShouldRespondWithCompanyNotFoundCode() throws Exception {
        String companyId = RandomStringUtils.randomNumeric(3);

        when(companyDAOMock.findById(anyString())).thenReturn(null);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/v1/companies/" + companyId))
                .andExpect(MockMvcResultMatchers.status()
                        .isNotFound())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cause")
                        .value("Company not found"));

        verify(companyDAOMock, times(1)).findById(anyString());
        verifyNoMoreInteractions(companyDAOMock);
    }

    @Test
    public void companyFindShouldRespondWithFoundCompany() throws Exception {
        String companyId = RandomStringUtils.randomNumeric(3);
        Company company = CompanyTestData.createCompany();

        company.setCid(companyId);

        when(companyDAOMock.findById(anyString())).thenReturn(company);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/v1/companies/" + companyId))
                .andExpect(MockMvcResultMatchers.status()
                        .isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cid")
                        .value(companyId));

        verify(companyDAOMock, times(1)).findById(anyString());
        verifyNoMoreInteractions(companyDAOMock);
    }

    @Test
    public void companyUpdateShouldRespondWithNotFoundCode() throws Exception {
        String companyId = RandomStringUtils.randomNumeric(3);

        when(companyDAOMock.findById(anyString())).thenReturn(null);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/v1/companies/" + companyId))
                .andExpect(MockMvcResultMatchers.status()
                        .isNotFound())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cause")
                        .value("Company not found"));

        verify(companyDAOMock, times(1)).findById(anyString());
        verifyNoMoreInteractions(companyDAOMock);
    }

    @Test
    public void companyUpdateWithoutIdShouldRespondWithNotFoundCode() throws Exception {
        Company company = CompanyTestData.createCompany();
        String companyJson = json(company);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/v1/companies/")
                        .contentType(contentType)
                        .content(companyJson))
                .andExpect(MockMvcResultMatchers.status()
                        .isMethodNotAllowed())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cause")
                        .value("Method not allowed"));

        verify(companyDAOMock, times(0)).findById(anyString());
        verifyNoMoreInteractions(companyDAOMock);
    }

    @Test
    public void companyUpdateWithInvalidIdShouldRespondWithNotFoundCode() throws Exception {
        Company company = CompanyTestData.createCompany();
        String companyJson = json(company);
        String companyId = RandomStringUtils.randomNumeric(3);

        when(companyDAOMock.findById(anyString())).thenReturn(null);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/v1/companies/" + companyId)
                        .contentType(contentType)
                        .content(companyJson))
                .andExpect(MockMvcResultMatchers.status()
                        .isNotFound())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cause")
                        .value("Company not found"));

        verify(companyDAOMock, times(1)).findById(anyString());
        verifyNoMoreInteractions(companyDAOMock);
    }

    @Test
    public void companyUpdateWithoutBodyShouldRespondWithInvalidRequestCode() throws Exception {
        String companyId = RandomStringUtils.randomNumeric(3);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/v1/companies/" + companyId)
                        .contentType(contentType))
                .andExpect(MockMvcResultMatchers.status()
                        .isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cause")
                        .value("Invalid request"));

        verify(companyDAOMock, times(0)).findById(anyString());
        verifyNoMoreInteractions(companyDAOMock);
    }

    @Test
    public void companyUpdateWithoutNameShouldRespondWithBadRequestCode() throws Exception {
        String companyId = RandomStringUtils.randomNumeric(3);
        Company company = CompanyTestData.createCompany();
        company.setName(null);
        company.setCid(companyId);
        String companyJson = json(company);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/v1/companies/" + companyId)
                        .contentType(contentType)
                        .content(companyJson))
                .andExpect(MockMvcResultMatchers.status()
                        .isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cause")
                        .value("Company 'name' property cannot be null nor empty"));

        verify(companyDAOMock, times(0)).findById(anyString());
        verifyNoMoreInteractions(companyDAOMock);
    }

    @Test
    public void companyUpdateWithMismatchingIdsShouldRespondWithUpdatedCompany() throws Exception {
        String companyId_1 = RandomStringUtils.randomNumeric(3);
        String companyId_2 = RandomStringUtils.randomNumeric(3);
        Company company = CompanyTestData.createCompany();
        company.setCid(companyId_1);
        String companyJson = json(company);

        when(companyDAOMock.findById(anyString())).thenReturn(company);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/v1/companies/" + companyId_2)
                        .contentType(contentType)
                        .content(companyJson))
                .andExpect(MockMvcResultMatchers.status()
                        .isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cid")
                        .value(companyId_2));

        verify(companyDAOMock, times(1)).findById(anyString());
        verify(companyDAOMock, times(1)).update(anyString(), any(Company.class));
        verifyNoMoreInteractions(companyDAOMock);
    }

    @Test
    public void companyUpdateShouldRespondWithUpdatedCompany() throws Exception {
        String companyId = RandomStringUtils.randomNumeric(3);
        Company company = CompanyTestData.createCompany();
        company.setCid(companyId);
        String companyJson = json(company);

        when(companyDAOMock.findById(anyString())).thenReturn(company);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/v1/companies/" + companyId)
                        .contentType(contentType)
                        .content(companyJson))
                .andExpect(MockMvcResultMatchers.status()
                        .isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cid")
                        .value(companyId));

        verify(companyDAOMock, times(1)).findById(anyString());
        verify(companyDAOMock, times(1)).update(anyString(), any(Company.class));
        verifyNoMoreInteractions(companyDAOMock);
    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}
