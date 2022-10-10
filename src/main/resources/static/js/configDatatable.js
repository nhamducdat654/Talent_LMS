$(document).ready(function() {
    $.fn.DataTable.ext.pager.simple_numbers_no_ellipses = function(page, pages){
        var numbers = [];
        var buttons = $.fn.DataTable.ext.pager.numbers_length;
        var half = Math.floor( buttons / 2 );

        var _range = function ( len, start ){
            var end;

            if ( typeof start === "undefined" ){
                start = 0;
                end = len;

            } else {
                end = start;
                start = len;
            }

            var out = [];
            for ( var i = start ; i < end; i++ ){ out.push(i); }

            return out;
        };


        if ( pages <= buttons ) {
            numbers = _range( 0, pages );

        } else if ( page <= half ) {
            numbers = _range( 0, buttons);

        } else if ( page >= pages - 1 - half ) {
            numbers = _range( pages - buttons, pages );

        } else {
            numbers = _range( page - half, page + half + 1);
        }

        numbers.DT_el = 'span';

        return [ 'previous', numbers, 'next' ];
    };
    $('#tableListUser').DataTable( {
        "paging":   true,
        "searching": false,
        "info": false,
        "lengthMenu": [[5, 10, 15, -1], [5, 10, 15, "All"]],
        oLanguage: {sLengthMenu: "<select style='margin-bottom: 10px;'>"+
                "<option value='5'>5</option>"+
                "<option value='10'>10</option>"+
                "<option value='15'>15</option>"+
                "<option value='-1'>All</option>"+
                "</select>"},
        "iDisplayLength": 5,
    } );

} );