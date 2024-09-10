package ani.rss.action;

import ani.rss.annotation.Auth;
import ani.rss.annotation.Path;
import ani.rss.entity.Ani;
import ani.rss.entity.Item;
import ani.rss.util.AniUtil;
import ani.rss.util.TorrentUtil;
import cn.hutool.http.server.HttpServerRequest;
import cn.hutool.http.server.HttpServerResponse;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Auth
@Path("/items")
public class ItemsAction implements BaseAction {
    @Override
    public void doAction(HttpServerRequest request, HttpServerResponse response) throws IOException {
        Ani ani = getBody(Ani.class);
        List<Item> items = AniUtil.getItems(ani);

        String downloadPath = TorrentUtil.getDownloadPath(ani).get(0).getAbsolutePath();

        for (Item item : items) {
            item.setLocal(false);
            File torrent = TorrentUtil.getTorrent(ani, item);
            if (torrent.exists()) {
                item.setLocal(true);
            }
            if (TorrentUtil.itemDownloaded(ani, item, false)) {
                item.setLocal(true);
            }
        }

        resultSuccess(Map.of(
                "downloadPath", downloadPath,
                "items", items
        ));
    }
}