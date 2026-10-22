<script lang="ts">
	import './layout.css';
	import favicon from '#lib/assets/favicon.svg';
	import type { LayoutProps } from './$types';
	import { resolve } from '$app/paths';

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
		<a href={resolve('/')} class="nav-link active">Home</a>
		<a href={resolve('/movies')} class="nav-link">Movies</a>
		<a href="" class="nav-link">Actors</a>
	</div>
</nav>

{@render children()}
</div>