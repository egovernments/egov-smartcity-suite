package org.egov.egi.web.controller;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import org.apache.tiles.request.render.StringRenderer;
import org.junit.Before;
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder;
import org.springframework.web.servlet.view.tiles3.TilesViewResolver;

public abstract class AbstractContextControllerTest<T> {
    protected StandaloneMockMvcBuilder mvcBuilder;
    protected T controller;

    @Before
    public void setUpBase() throws Exception {
        this.controller = initController();

        TilesViewResolver tilesViewResolver = new TilesViewResolver();
        tilesViewResolver.setRenderer(new StringRenderer());

        mvcBuilder = standaloneSetup(controller)
                .setViewResolvers(tilesViewResolver);

    }

    protected abstract T initController();
}
