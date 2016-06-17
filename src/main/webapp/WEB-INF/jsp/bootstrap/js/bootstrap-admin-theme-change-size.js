$(function() {
    var sizeChangerContainer = '.bootstrap-admin-theme-change-size';

    $('.size-changer', sizeChangerContainer).on('click', function() {
        var setLargeSize = $(this).hasClass('large');

        if (setLargeSize && $('link[href^="css/bootstrap-admin-theme"]').length === 2) {
            return false;
        }

        $(this).addClass('active');

        if (setLargeSize) {
            $('link[href="css/bootstrap-admin-theme-small.css"]').remove();
            $('link[href="css/bootstrap-small.css"]').remove();

            $('.small', sizeChangerContainer).removeClass('active');
            return true;
        }

        $('head').append('<link rel="stylesheet" media="screen" href="css/bootstrap-small.css"><link rel="stylesheet" media="screen" href="css/bootstrap-admin-theme-small.css">');
        $('.large', sizeChangerContainer).removeClass('active');
    });
});