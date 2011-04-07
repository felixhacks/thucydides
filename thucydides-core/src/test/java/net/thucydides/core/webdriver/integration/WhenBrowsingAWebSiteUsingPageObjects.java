package net.thucydides.core.webdriver.integration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

import java.io.File;
import java.util.Set;

import net.thucydides.core.pages.PageObject;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.interactions.ElementNotDisplayedException;

public class WhenBrowsingAWebSiteUsingPageObjects {

    public class IndexPage extends PageObject {

        public WebElement multiselect;

        public WebElement checkbox;
        
        public IndexPage(WebDriver driver) {
            super(driver);
        }


    }
    
    WebDriver driver;
    
    @Before
    public void open_local_static_site() {
        driver = new HtmlUnitDriver();
        openStaticTestSite(driver);
    }

    private void openStaticTestSite(WebDriver driver) {
        File baseDir = new File(System.getProperty("user.dir"));
        File testSite = new File(baseDir,"src/test/resources/static-site/index.html");
        this.driver.get("file://" + testSite.getAbsolutePath());
    }

    @Test
    public void should_find_page_title() {
        IndexPage indexPage = new IndexPage(driver);
        assertThat(indexPage.getTitle(), is("Thucydides Test Site"));
    } 

    @Test
    public void should_find_text_contained_in_page() {
        IndexPage indexPage = new IndexPage(driver);
        indexPage.shouldContainText("Some test pages");
    }

    @Test(expected=NoSuchElementException.class)
    public void should_not_find_text_not_contained_in_page() {
        IndexPage indexPage = new IndexPage(driver);
        indexPage.shouldContainText("This text is not in the pages");
    }
    
    @Test
    public void should_select_in_multiple_select_lists_correctly() {
        IndexPage indexPage = new IndexPage(driver);
        indexPage.selectMultipleItemsFromDropdown(indexPage.multiselect,"Label 1", "Label 3");
        
        Set<String> selectedLabels = indexPage.getSelectedOptionLabelsFrom(indexPage.multiselect);
        assertThat(selectedLabels.size(), is(2));
        assertThat(selectedLabels, hasItems("Label 1", "Label 3"));
    }
    
    @Test
    public void should_select_values_in_multiple_select_lists_correctly() {
        IndexPage indexPage = new IndexPage(driver);
        indexPage.selectMultipleItemsFromDropdown(indexPage.multiselect,"Label 1", "Label 3");
        
        Set<String> selectedValues = indexPage.getSelectedOptionValuesFrom(indexPage.multiselect);
        assertThat(selectedValues.size(), is(2));
        assertThat(selectedValues, hasItems("1", "3"));
    }
    
    @Test
    public void ticking_an_empty_checkbox_should_set_the_value_to_true() {
        IndexPage indexPage = new IndexPage(driver);
        indexPage.setCheckbox(indexPage.checkbox, true);
        
        assertThat(indexPage.checkbox.isSelected(), is(true));
    }
    
    @Test
    public void ticking_a_set_checkbox_should_set_the_value_to_true() {
        IndexPage indexPage = new IndexPage(driver);
        indexPage.checkbox.setSelected();
        
        indexPage.setCheckbox(indexPage.checkbox, true);
        
        assertThat(indexPage.checkbox.isSelected(), is(true));
    }
    
    @Test
    public void unticking_an_unset_checkbox_should_set_the_value_to_false() {
        IndexPage indexPage = new IndexPage(driver);
        
        indexPage.setCheckbox(indexPage.checkbox, false);
        
        assertThat(indexPage.checkbox.isSelected(), is(false));
    }
    
    @Test
    public void unticking_a_set_checkbox_should_set_the_value_to_false() {
        IndexPage indexPage = new IndexPage(driver);
        indexPage.checkbox.setSelected();
        
        indexPage.setCheckbox(indexPage.checkbox, false);
        
        assertThat(indexPage.checkbox.isSelected(), is(false));
    }
    
    
    @Test
    public void should_know_when_text_appears_on_a_page() {
        IndexPage indexPage = new IndexPage(driver);
        indexPage.waitForTextToAppear("Label 1");
    }
        
    @Test(expected=ElementNotDisplayedException.class)
    public void should_fail_if_text_does_not_appear_on_a_page() {
        IndexPage indexPage = new IndexPage(driver);
        indexPage.setWaitForTimeout(100);

        indexPage.waitForTextToAppear("Label that is not present");
    }
    
    @Test
    public void should_know_when_one_of_several_texts_appears_on_a_page() {
        IndexPage indexPage = new IndexPage(driver);
        indexPage.waitForAnyTextToAppear("Label 1", "Label that is not present");
    }

    @Test(expected=ElementNotDisplayedException.class)
    public void should_fail_if_the_requested_text_is_not_on_the_page() {
        IndexPage indexPage = new IndexPage(driver);
        indexPage.setWaitForTimeout(100);

        indexPage.waitForAnyTextToAppear("Label that is not present");
    }

    @Test
    public void should_know_when_all_of_a_set_of_texts_appears_on_a_page() {
        IndexPage indexPage = new IndexPage(driver);
        indexPage.setWaitForTimeout(100);

        indexPage.waitForAllTextToAppear("Label 1", "Label 2");
    }

    @Test(expected=ElementNotDisplayedException.class)
    public void should_fail_if_one_of_a_set_of_requested_texts_does_not_appear_on_a_page() {
        IndexPage indexPage = new IndexPage(driver);
        indexPage.setWaitForTimeout(100);

        indexPage.waitForAllTextToAppear("Label 1", "Label that is not present");
    }

    @Test(expected=ElementNotDisplayedException.class)
    public void should_fail_if_none_of_the_requested_texts_appear_on_a_page() {
        IndexPage indexPage = new IndexPage(driver);
        indexPage.setWaitForTimeout(100);

        indexPage.waitForAllTextToAppear("Label that is not present", "Another label that is not present");
    }
}
