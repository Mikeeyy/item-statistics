package com.matejko.service.impl.parsers;

import com.matejko.service.interfaces.parsers.WebParserService;
import com.matejko.utils.PhantomJsUtils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.nodes.Document;

/**
 * Created by Mikołaj Matejko on 19.08.2017 as part of offer-lookup
 */
public abstract class BaseWebParserService implements WebParserService {
  @Override
  public Document navigate(final String url) {
    final Pattern pattern = Pattern.compile(".*https:((\\\\\\\\)|(//)).*");
    final Matcher matcher = pattern.matcher(url);

    final String urlToRender;
    if (matcher.matches()) {
      urlToRender = url;
    } else {
      urlToRender = "https://" + url;
    }

    return PhantomJsUtils.renderPage(urlToRender);
  }

  @Override
  public String createNextPageUrl(final String providedUrl, final Long currentPage) {
    final Pattern questionMarkPattern = Pattern.compile(".*\\?.*");
    final Matcher questionMarkMatcher = questionMarkPattern.matcher(providedUrl);

    if (!questionMarkMatcher.matches()) {
      return providedUrl + "?" + queryPageParam() + "=" + (currentPage + 1);
    }

    final Pattern existingParamPattern = Pattern.compile("(.*[?&])(" + queryPageParam() + ")(=[0-9]+.*)");
    final Matcher existingParamMatcher = existingParamPattern.matcher(providedUrl);

    if (existingParamMatcher.matches()) {
      return existingParamMatcher.group(0) + (currentPage + 1) + existingParamMatcher.group(2);
    }

    return providedUrl + "&" + queryPageParam() + "=" + (currentPage + 1);
  }

  protected abstract String queryPageParam();
}
