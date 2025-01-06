import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HeaderComponent } from "./header/header.component";
import { FooterComponent } from './footer/footer.component';
import { FormPostComponent} from "./form-post/form-post.component";
import { ForumComponent } from "./forum/forum.component";
import { PostComponent } from "./post/post.component";

@Component({
	selector: 'app-root',
	standalone: true,
	imports: [RouterOutlet, HeaderComponent, FooterComponent, FormPostComponent, ForumComponent, PostComponent],
	templateUrl: './app.component.html',

	styleUrls: ['./app.component.css'],

})

export class AppComponent {
	title = 'diversify-fe';
}