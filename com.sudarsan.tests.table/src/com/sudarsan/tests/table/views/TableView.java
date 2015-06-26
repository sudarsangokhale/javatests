package com.sudarsan.tests.table.views;


import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;


/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */

public class TableView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.sudarsan.tests.table.views.TableView";

	private TableViewer viewer;

	/*
	 * The content provider class is responsible for
	 * providing objects to the view. It can wrap
	 * existing objects in adapters or simply return
	 * objects as-is. These objects may be sensitive
	 * to the current input of the view, or ignore
	 * it and always show the same content 
	 * (like Task List, for example).
	 */
	 
	class ViewContentProvider implements IStructuredContentProvider {
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
		public void dispose() {
		}
		public Object[] getElements(Object parent) {
			return new String[] { "One", "Two", "Three" };
		}
	}
	
	public Image getImage(Object obj) {
		return PlatformUI.getWorkbench().
				getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
	}
	public Image getErrorImage(Object obj) {
		return PlatformUI.getWorkbench().
				getSharedImages().getImage(ISharedImages.IMG_OBJS_ERROR_TSK);
	}
	public Image getWarningImage(Object obj) {
		return PlatformUI.getWorkbench().
				getSharedImages().getImage(ISharedImages.IMG_OBJS_WARN_TSK);
	}
	
	class FirstColumnLabelProvider extends ColumnLabelProvider {
		
		@Override
		public Image getImage(Object element) {
			if("Two".equals(element))
				return getErrorImage(element);
			return null;
		}
		
		@Override
		public String getText(Object element) {
			return null;
		}
		
		@Override
		public Image getToolTipImage(Object object) {
			if("Two".equals(object)){
				return getErrorImage(object);
			}
			return null;
		}
		
		@Override
		public String getToolTipText(Object element) {
			return "Tooltip " + getText(element);  
		}
		
		@Override
		public Point getToolTipShift(Object object) {
			return new Point(5, 5);
		}
		
		@Override
		public int getToolTipTimeDisplayed(Object object) {
			return 5000;
		}
		
		@Override
		public int getToolTipDisplayDelayTime(Object object) {
			return 100;
		}
	}
	
	class SecondColumnLabelProvider extends FirstColumnLabelProvider {

		@Override
		public Image getImage(Object element) {
			return null;
		}

		@Override
		public String getText(Object element) {
			return (element != null)?element.toString():null;
		}
		
	}
	
	class ViewTableLabelProvider extends LabelProvider implements ITableLabelProvider {
		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			if(columnIndex == 0){
				if("Two".equals(element))
					return getErrorImage(element);
			}
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			if(columnIndex == 1){
				return (element != null)?element.toString():null;
			}
			return null;
		}
	}
	
	class NameSorter extends ViewerSorter {
	}

	/**
	 * The constructor.
	 */
	public TableView() {
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);
		// Tooltip support
		ColumnViewerToolTipSupport.enableFor(viewer, ToolTip.NO_RECREATE);
		
		// Table settings
		Table table = viewer.getTable();
		table.setVisible(true);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		// Set the columns
		TableViewerColumn c1 = new TableViewerColumn(viewer, SWT.CENTER);
		c1.getColumn().setWidth(20);
		c1.getColumn().setAlignment(SWT.CENTER);
		c1.setLabelProvider(new FirstColumnLabelProvider());
		
		TableViewerColumn c2 = new TableViewerColumn(viewer, SWT.LEFT | SWT.RESIZE);
		c2.getColumn().setText("Header");
		c2.getColumn().setWidth(200);
		c2.setLabelProvider(new SecondColumnLabelProvider());
		
		viewer.setContentProvider(new ViewContentProvider());
		//viewer.setLabelProvider(new ViewTableLabelProvider());
		viewer.setSorter(new NameSorter());
		viewer.setInput(getViewSite());
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
}