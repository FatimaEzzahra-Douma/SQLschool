/*******************************************************************************
 * 	Java tool with a GUI to help learn SQL
 * 	
 *     Copyright (C) 2020  Bayad Nasr-eddine, Bayol Thibaud, Benazzi Naima, 
 *     Douma Fatima Ezzahra, Chaouche Sonia, Kanyamibwa Blandine
 *     (thesqlschool@hotmail.com)
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *******************************************************************************/
package apprendreSQL.Controller;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

/**
 * This class watches for any change on .../resource and executes an action accordingly.
 *
 */
public class Watcher {

	private WatchService watcher;
	private Path directory;
	private EventManager manager;

	public Watcher(EventManager manager) {
		directory = Paths.get("resource/");
		this.manager = manager;
	}

	/**
	 * Watches for changes on the 'resource' directory.
	 */
	@SuppressWarnings("rawtypes")
	protected void observe() {
		try {
			this.watcher = FileSystems.getDefault().newWatchService();
			directory.register(watcher, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE,
					StandardWatchEventKinds.ENTRY_MODIFY);

			boolean valid = true;
			do {
				WatchKey watchKey = watcher.take();

				for (WatchEvent event : watchKey.pollEvents()) {
					if (StandardWatchEventKinds.ENTRY_CREATE.equals(event.kind())) {
						String fileName = event.context().toString();
						manager.updateExercisesView();
						System.out.println("File Created: " + fileName);
					}

					if (StandardWatchEventKinds.ENTRY_DELETE.equals(event.kind())) {
						String fileName = event.context().toString();
						manager.updateExercisesView();
						System.out.println("File Deleted: " + fileName);
					}

					if (StandardWatchEventKinds.ENTRY_MODIFY.equals(event.kind())) {
						String fileName = event.context().toString();
						if (fileName.endsWith(".json"))
							manager.updateExercisesView();
						System.out.println("File Modified: " + fileName);
					}
				}
				valid = watchKey.reset();

			} while (valid);
		} catch (IOException | InterruptedException e1) {
			e1.printStackTrace();
		}

	}

}
