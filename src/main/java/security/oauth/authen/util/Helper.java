package security.oauth.authen.util;

import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.springframework.context.i18n.LocaleContextHolder;



public class Helper {
	public static String currentLocale() {
        Locale currentLocale = LocaleContextHolder.getLocale();
        if (currentLocale.getLanguage().equals("th")) {
            return "th";
        } else {
            return "en";
        }
    }

    public static String localizedField(String name) {
        if (Helper.currentLocale().equals("th")) {
            return name + "Th";
        } else {
            return name;
        }
    }

    public static String getMessage(String code) {
        MessageByLocaleService messageByLocaleService = SpringContextUtil.getApplicationContext().getBean(MessageByLocaleService.class);
        return messageByLocaleService.getMessage(code);
    }

    public static Locale currentLocale(HttpServletRequest request) {
        String lang = "en";
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("myI18N_cookie")) {
                    lang = cookie.getValue();
                }
            }
        }

        if (lang.equals("th") || lang.equals("en")) {
            return new Locale(lang);
        } else {
            return new Locale("en");
        }
    }

}
