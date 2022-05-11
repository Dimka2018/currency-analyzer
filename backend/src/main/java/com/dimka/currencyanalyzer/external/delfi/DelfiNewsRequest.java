package com.dimka.currencyanalyzer.external.delfi;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class DelfiNewsRequest {

    private String query = "fragment channelData on Channel {\n    id\n    external_id\n    name\n}\n\nfragment articleData on ArticleWithPager {\n    items {\n        id\n        external_id\n        title\n        publish_time\n        web_url\n        channels {\n            ...channelData\n        }\n        primary_category {\n          name\n        }\n        comments {\n            count\n        }\n        tags {\n            slug\n            name\n            domain {\n                id\n                channel {\n                    ...channelData\n                }\n                channel_rus {\n                    ...channelData\n                }\n            }\n        }\n        pictures {\n            normal: url(size: w300h452)\n            thumb: url(size: w80h80)\n            org: url\n            caption\n        }\n    }\n    pager {\n        count\n        limit\n        offset\n    }\n}\n\nquery (\n$channelsExternalId: [String]\n$categoriesExternalId: [String]\n$search: String\n$publishTimeFrom: Date\n$publishTimeTo: Date\n$limit: Int\n) {\n    getArticles(\n        channelsExternalId: $channelsExternalId,\n        categoriesExternalId: $categoriesExternalId,\n        search: $search,\n        publishTimeFrom: $publishTimeFrom,\n        publishTimeTo: $publishTimeTo,\n        limit: $limit,\n        order: LATEST\n    ) {\n        ...articleData\n    }\n}\n\n";
    private DelfiRequestVariables variables;
}
