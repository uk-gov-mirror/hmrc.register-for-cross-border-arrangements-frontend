$(document).ready(function() {

  // =====================================================
  // Handle the UR recruitmentBanner
  // =====================================================
     recruitmentBanner()

  // =====================================================
  // Initialise show-hide-content
  // Toggles additional content based on radio/checkbox input state
  // =====================================================
  var showHideContent, mediaQueryList;
  showHideContent = new GOVUK.ShowHideContent()
  showHideContent.init()

  // =====================================================
  // Use GOV.UK shim-links-with-button-role.js to trigger
  // links with role="button" when space key is pressed
  // =====================================================
  GOVUK.shimLinksWithButtonRole.init();

  // =====================================================
  // Handle number inputs
  // =====================================================
    numberInputs();

  // =====================================================
  // Introduce direct skip link control, to work around voiceover failing of hash links
  // https://bugs.webkit.org/show_bug.cgi?id=179011
  // https://axesslab.com/skip-links/
  // =====================================================
  $('.skiplink').click(function(e) {
    e.preventDefault();
    $(':header:first').attr('tabindex', '-1').focus();
  });

  //======================================================
  // Move immediate forcus to any error summary
  //======================================================
  if ($('.error-summary a').length > 0){
    $('.error-summary').focus();
  }

  // =====================================================
  // Adds data-focuses attribute to all containers of inputs listed in an error summary
  // This allows validatorFocus to bring viewport to correct scroll point
  // =====================================================
      function assignFocus () {
          var counter = 0;
          $('.error-summary-list a').each(function(){
              var linkhash = $(this).attr("href").split('#')[1];
              $('#' + linkhash).parents('.form-field, .form-group').first().attr('id', 'f-' + counter);
              $(this).attr('data-focuses', 'f-' + counter);
              counter++;
          });
      }
      assignFocus();

    // =====================================================
    // Print functionality
    // Opens any details components so they are printed
    // =====================================================
      function beforePrintCall(){
          if($('.no-details').length > 0){
              // store current focussed element to return focus to later
              var fe = document.activeElement;
              // store scroll position
              var scrollPos = window.pageYOffset;
              $('details').not('.open').each(function(){
                  $(this).addClass('print--open');
                  $(this).find('summary').trigger('click');
              });
              // blur focus off current element in case original cannot take focus back
              $(document.activeElement).blur();
              // return focus if possible
              $(fe).focus();
              // return to scroll pos
              window.scrollTo(0,scrollPos);
          } else {
              $('details').attr("open","open").addClass('print--open');
          }
          $('details.print--open').find('summary').addClass('heading-medium');
      }

      function afterPrintCall(){
          $('details.print--open').find('summary').removeClass('heading-medium');
          if($('.no-details').length > 0){
              // store current focussed element to return focus to later
              var fe = document.activeElement;
              // store scroll position
              var scrollPos = window.pageYOffset;
              $('details.print--open').each(function(){
                  $(this).removeClass('print--open');
                  $(this).find('summary').trigger('click');
              });
              // blur focus off current element in case original cannot take focus back
              $(document.activeElement).blur();
              // return focus if possible
              $(fe).focus();
              // return to scroll pos
              window.scrollTo(0,scrollPos);
          } else {
              $('details.print--open').removeAttr("open").removeClass('print--open');
          }
      }

      //Chrome
      if(typeof window.matchMedia != 'undefined'){
          mediaQueryList = window.matchMedia('print');
          mediaQueryList.addListener(function(mql) {
              if (mql.matches) {
                  beforePrintCall();
              };
              if (!mql.matches) {
                  afterPrintCall();
              };
          });
      }

      //Firefox and IE (above does not work)
      window.onbeforeprint = function(){
          beforePrintCall();
      }
      window.onafterprint = function(){
          afterPrintCall();
      }
  });


  function numberInputs() {
      // =====================================================
      // Set currency fields to number inputs on touch devices
      // this ensures on-screen keyboards display the correct style
      // don't do this for FF as it has issues with trailing zeroes
      // =====================================================
      if($('html.touchevents').length > 0 && window.navigator.userAgent.indexOf("Firefox") == -1){
          $('[data-type="currency"] > input[type="text"], [data-type="percentage"] > input[type="text"]').each(function(){
            $(this).attr('type', 'number');
            $(this).attr('step', 'any'); 
            $(this).attr('min', '0');
          });
      }

      // =====================================================
      // Disable mouse wheel and arrow keys (38,40) for number inputs to prevent mis-entry
      // also disable commas (188) as they will silently invalidate entry on Safari 10.0.3 and IE11
      // =====================================================
      $("form").on("focus", "input[type=number]", function(e) {
          $(this).on('wheel', function(e) {
              e.preventDefault();
          });
      });
      $("form").on("blur", "input[type=number]", function(e) {
          $(this).off('wheel');
      });
      $("form").on("keydown", "input[type=number]", function(e) {
          if ( e.which == 38 || e.which == 40 || e.which == 188 )
              e.preventDefault();
 });
}

function recruitmentBanner()
{
    const recruitmentBanner = $("#recruitment-banner")

    if(recruitmentBanner){
        const recruitmentBannerDismiss = $("#recruitment-banner-dismiss")

        const recruitmentCookieName = "mtdpurr"
        const hasDismissed = getCookie(recruitmentCookieName)

        if (hasDismissed) {
            recruitmentBanner.remove()
        } else {
            recruitmentBannerDismiss.click(function(event) {
                event.preventDefault()
                setCookie(recruitmentCookieName, 'suppress_for_all_services', { days: 30 })
                recruitmentBanner.remove()
            })
            recruitmentBanner.removeClass('js-hidden');
        }
    }
}

function setCookie(name, value, duration, domain) {
    var secure = window.location.protocol.indexOf('https') ? '' : '; secure'
    var cookieDomain = ''
    var expires = ''

    if (domain) {
        cookieDomain = '; domain=' + domain
    }

    if (duration) {
        var date = new Date()
        date.setTime(date.getTime() + (duration.days * 24 * 60 * 60 * 1000))
        expires = '; expires=' + date.toGMTString()
    }

    document.cookie = name + '=' + value + expires + cookieDomain + '; path=/' + secure
}

function getCookie(name) {
    var i, c
    var nameEQ = name + '='
    var ca = document.cookie.split(';')

    for (i = 0; i < ca.length; i += 1) {
        c = ca[i]

        while (c.charAt(0) === ' ') {
            c = c.substring(1, c.length)
        }

        if (c.indexOf(nameEQ) === 0) {
            return c.substring(nameEQ.length, c.length)
        }
    }

    return null
}
