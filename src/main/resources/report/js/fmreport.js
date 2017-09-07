var windowElement = $(window);
var htmlBodyElement = $('html,body');
var headerElement = $('#header');
var sourceDirectoryElement = $('#sourceDirectory');
var headerTitleContainerElement = $('#headerTitleContainer');

var returnToTopElement = $('#returnToTop');

windowElement.load(function() {
	initWindow();
	initTables();
	initReturnToTopElement();
	displayWindowSize();
});

windowElement.resize(function() {
	refreshReturnToTopElement();
	displayWindowSize();
});

$(document).scroll(function onScroll(event){
	refreshReturnToTopElement();
});

function initWindow() {

	htmlBodyElement.css({ visibility: 'visible' });

	AOS.init({
		easing: 'ease-in-out-sine'
	});

}

function initReturnToTopElement() {

	returnToTopElement.click(function() {
		htmlBodyElement.animate({
	        scrollTop : 0
	    }, 500);
	});

	refreshReturnToTopElement();

}

function refreshReturnToTopElement() {
	if ($(document).scrollTop() >= headerElement.height()) {
		returnToTopElement.fadeIn(200);
	} else {
		returnToTopElement.fadeOut(200);
	}
}

function displayWindowSize() {
// console.log('window : height=' + $(window).height() + ', width=' + $(window).width());
}

function openDirectory(directoryPath) {
	window.open(directoryPath.innerText);
}
function openSourceDirectory(directoryRelativePathElement) {
	window.open(sourceDirectoryElement.text() + directoryRelativePathElement.innerText);
}
function openFile(directoryElement, fileRelativePath) {
	var filePath = 'file://' + directoryElement.text() + '/' + fileRelativePath;
	window.open(filePath, '_blank');
}
function openSourceFile(fileRelativePathElement) {
	openFile(sourceDirectoryElement, fileRelativePathElement.innerText);
}
function openMovedSourceFile(fileRelativePath) {
	openFile(sourceDirectoryElement, fileRelativePath);
}
function openTargetFile(fileRelativePathElement) {
	openFile($('#targetDirectory'), fileRelativePathElement.innerText);
}
function initTables() {

	$('.datatable').each(function () {

		$(this).find('tfoot th').each( function () {
	        var title = $(this).text();
	        $(this).html( '<input type="text" placeholder="Search ' + title + '" />' );
	    });

		var table = $(this).DataTable(
										{
											"lengthMenu": [[10, 25, 50, 100, 1000, -1], [10, 25, 50, 100, 1000, "All"]]
										}
									);

		table.columns().every( function () {
	        var that = this;
	 
	        $( 'input', this.footer() ).on( 'keyup change', function () {
	            if ( that.search() !== this.value ) {
	                that
	                    .search( this.value )
	                    .draw();
	            }
	        });
	    });
	    
	    
    });

}

var _empty = function ( d ) {
	return !d || d === true || d === '-' ? true : false;
};

jQuery.extend(jQuery.fn.dataTableExt.oSort, {
	"string-pre": function ( a ) {

		if (a.match(/^\d[\d\s]*\sbytes.+$/)) {
			return parseInt(a.substring(0, a.indexOf('bytes')).replace(/\s/g, ''));
		}

		// This is a little complex, but faster than always calling toString,
		// http://jsperf.com/tostring-v-check
		return _empty(a) ?
			'' :
			typeof a === 'string' ?
				a.toLowerCase() :
				! a.toString ?
					'' :
					a.toString();
	}
});