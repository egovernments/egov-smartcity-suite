package org.egov.pgrweb.controller;

import org.apache.tiles.request.render.StringRenderer;
import org.junit.Before;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.view.tiles3.TilesViewResolver;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public abstract class AbstractContextControllerTest<T> {
    protected MockMvc mockMvc;
    protected T controller;

    @Before
    public void setUpBase() throws Exception {
        this.controller = initController();

        TilesViewResolver tilesViewResolver = new TilesViewResolver();
        tilesViewResolver.setRenderer(new StringRenderer());
        this.mockMvc = standaloneSetup(controller)
                .setViewResolvers(tilesViewResolver)
                .build();

    }

    protected abstract T initController();
}
