package ani.rss.action;

import ani.rss.annotation.Auth;
import ani.rss.annotation.Path;
import ani.rss.entity.Config;
import cn.hutool.core.codec.Base64;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.server.HttpServerRequest;
import cn.hutool.http.server.HttpServerResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@Auth
@Path("/proxy")
public class ProxyAction implements BaseAction {

    @Override
    public void doAction(HttpServerRequest request, HttpServerResponse response) throws IOException {
        String url = request.getParam("url");
        Config config = getBody(Config.class);
        String proxyHost = config.getProxyHost();
        int proxyPort = config.getProxyPort();
        Boolean proxy = config.getProxy();

        url = Base64.decodeStr(url);

        log.info(url);

        HttpRequest httpRequest = HttpRequest.get(url)
                .setFollowRedirects(true);

        if (proxy) {
            httpRequest.setHttpProxy(proxyHost, proxyPort);
        }

        Integer status = httpRequest
                .thenFunction(HttpResponse::getStatus);
        resultSuccess(status);
    }
}