---
title: "Search"
description: "Get instant and faceted search results from Segmentify and report clicks on them."
---

Segmentify Search has two modes. **Instant search** answers while the user types, in the search box. **Faceted search** returns a full results screen with filters, sorting and paging.

## Instant search

Send the current text each time it changes. Send an empty `query` when the search box opens, to get the "before search" content such as popular products and last searches.

```kotlin
import com.segmentify.segmentifyandroidsdk.model.SearchPageModel
import com.segmentify.segmentifyandroidsdk.model.SearchResponseModel

val search = SearchPageModel()
search.query = "sneak"      // empty string before the user types
search.lang = "EN"
search.currency = "EUR"

SegmentifyManager.sendSearchPageView(search, object : SegmentifyCallback<SearchResponseModel> {
    override fun onDataLoaded(data: SearchResponseModel) {
        // data.products, data.keywords, data.categories, data.brands, data.lastSearches
    }
})
```

The response holds `products`, plus `keywords`, `categories`, `brands`, `categoryProducts`, `brandProducts` and `lastSearches` to build the search box's sections.

## Faceted search

```kotlin
import com.segmentify.segmentifyandroidsdk.model.faceted.Filter
import com.segmentify.segmentifyandroidsdk.model.faceted.Ordering
import com.segmentify.segmentifyandroidsdk.model.faceted.SearchFacetedPageModel
import com.segmentify.segmentifyandroidsdk.model.faceted.SearchFacetedResponseModel

val search = SearchFacetedPageModel()      // type is "faceted", trigger is "page" by default
search.query = "sneakers"
search.trigger = "keyword"                 // what caused the search: keyword, filter, order or page
search.ordering = Ordering("SMART_SORTING", 1)
search.filters = listOf(Filter("brand", listOf("Acme")))
search.lang = "EN"
search.currency = "EUR"

SegmentifyManager.sendFacetedSearchPageView(search, object : SegmentifyCallback<SearchFacetedResponseModel> {
    override fun onDataLoaded(data: SearchFacetedResponseModel) {
        // data.products, data.facets, data.meta, data.banners, data.instanceId
    }
})
```

| Property | Description |
| --- | --- |
| `query` | The text searched. |
| `trigger` | What caused this request: `keyword` (a new search), `filter` (a filter changed), `order` (the sort changed) or `page` (another page). |
| `ordering` | The page number, starting at 1, and the sort. |
| `filters` | The selected filters, each a facet name and its selected values. |

The response holds `products`, `facets` (the available filters and their values), `meta` (paging and totals), `banners` and the `instanceId` of the search campaign.

## Report clicks

When the user taps a product in search results, report it with the search campaign's `instanceId` and the product ID:

```kotlin
SegmentifyManager.sendSearchClickView(instanceId, "SKU-1001")
```

Report when the results are shown with `sendImpression` and `sendWidgetView`, as for recommendation widgets. See [Interactions and Banners](/events/interactions/).

## Clear a last search

When the user deletes an entry from their last searches:

```kotlin
SegmentifyManager.clearLastSearchHistory("sneakers")
```
