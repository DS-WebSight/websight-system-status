package pl.ds.websight.system.status.servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.jetbrains.annotations.NotNull;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

@Component(service = Servlet.class)
@SlingServletPaths("/apps/websight-system-status/bin/bundles-version-list")
public class GetBundleVersionReportServlet extends SlingSafeMethodsServlet {

    private static final String BUNDLE_CATEGORY = "websight";

    @Override
    public void doGet(@NotNull SlingHttpServletRequest request, @NotNull SlingHttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        BundleContext bundleContext = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
        PrintWriter writer = response.getWriter();
        writer.print("[");
        boolean isNotFirst = false;
        for (Bundle bundle : bundleContext.getBundles()) {
            if (BUNDLE_CATEGORY.equals(bundle.getHeaders().get("Bundle-Category"))) {
                if (isNotFirst) {
                    writer.print(",");
                }
                writer.print(String.format("{\"name\":\"%s\",\"version\":\"%s\"}", bundle.getSymbolicName(),
                        bundle.getVersion().toString()));
                isNotFirst = true;
            }
        }
        writer.print("]");
    }
}