<script lang="ts">
	import './layout.css';
	import favicon from '#lib/assets/favicon.svg';
	import type { LayoutProps } from './$types';
	import { resolve } from '$app/paths';
	import { page } from '$app/state';

	let { children }: LayoutProps = $props();
</script>

<svelte:head>
	<link rel="icon" href={favicon} />
</svelte:head>

<div class="site-wrapper">
<nav class="navbar">
	<a href={resolve('/')} class="navbar-brand">
		<span class="brand-icon">▶</span>
		<span>Great<span class="brand-highlight">Movies</span></span>
	</a>

	<div class="navbar-links">
		<a href={resolve('/')} class="nav-link {page.url.pathname == '/' ? 'active' : ''}">Home</a>
		<a href={resolve('/movies')} class="nav-link {page.url.pathname.startsWith('/movies') ? 'active' : ''}">Movies</a>
		<a href={resolve('/cast-crew')} class="nav-link {page.url.pathname.startsWith('/cast-crew') ? 'active' : ''}">Cast and Crew</a>
	</div>
</nav>

{@render children()}
</div>